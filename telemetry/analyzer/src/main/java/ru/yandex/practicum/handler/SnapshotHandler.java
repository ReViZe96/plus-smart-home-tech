package ru.yandex.practicum.handler;

import com.google.protobuf.Timestamp;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.grpc.telemetry.event.ActionTypeProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionRequest;
import ru.yandex.practicum.grpc.telemetry.hubrouter.HubRouterControllerGrpc;
import ru.yandex.practicum.handler.event.sensor.SensorEventHandler;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;
import ru.yandex.practicum.model.Action;
import ru.yandex.practicum.model.Condition;
import ru.yandex.practicum.model.ConditionOperator;
import ru.yandex.practicum.model.Scenario;
import ru.yandex.practicum.repository.ScenarioRepository;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class SnapshotHandler {

    @GrpcClient("hub-router")
    private HubRouterControllerGrpc.HubRouterControllerBlockingStub hubRouterClient;

    private final ScenarioRepository scenarioRepository;
    private final Map<String, SensorEventHandler> sensorHandlers;

    public SnapshotHandler(ScenarioRepository scenarioRepository,
                           Set<SensorEventHandler> sensorHandlers) {
        this.scenarioRepository = scenarioRepository;
        this.sensorHandlers = sensorHandlers.stream()
                .collect(Collectors.toMap(
                        SensorEventHandler::getSensorType,
                        Function.identity()
                ));
    }


    public void handle(SensorsSnapshotAvro snapshot) {

        List<Scenario> currentHubscenarioList = scenarioRepository.findByHubId(snapshot.getHubId());
        Map<String, SensorStateAvro> snapshotSensorsStates = snapshot.getSensorsState();

        List<Scenario> scenariosWithCorrectConditions = currentHubscenarioList.stream()
                .filter(hubScenario -> isConditionsCorrect(hubScenario.getConditions(), snapshotSensorsStates))
                .toList();

        for (Scenario hubScenario : scenariosWithCorrectConditions) {
            List<Action> actions = hubScenario.getActions();
            for (Action action : actions) {
                DeviceActionProto actionProto = DeviceActionProto.newBuilder()
                        .setSensorId(action.getSensor().getId())
                        .setType(ActionTypeProto.valueOf(action.getType().name()))
                        .setValue(action.getValue())
                        .build();

                Timestamp timestamp = Timestamp.newBuilder()
                        .setSeconds(Instant.now().getEpochSecond())
                        .setNanos(Instant.now().getNano())
                        .build();

                DeviceActionRequest request = DeviceActionRequest.newBuilder()
                        .setHubId(hubScenario.getHubId())
                        .setScenarioName(hubScenario.getName())
                        .setTimestamp(timestamp)
                        .setAction(actionProto)
                        .build();

                hubRouterClient.handleDeviceAction(request);
            }
        }
    }


    private boolean isConditionsCorrect(List<Condition> conditions, Map<String, SensorStateAvro> sensorStates) {
        if (sensorStates == null || sensorStates.isEmpty())
            return false;
        if (conditions == null || conditions.isEmpty())
            return true;
        return conditions.stream()
                .allMatch(
                        condition -> isConditionCorrect(condition, sensorStates.get(condition.getSensor().getId())));

    }

    private boolean isConditionCorrect(Condition condition, SensorStateAvro sensorState) {
        if (condition == null || sensorState == null || sensorState.getData() == null)
            return false;

        String sensorType = sensorState.getData().getClass().getName();
        if (!sensorHandlers.containsKey(sensorType)) {
            throw new RuntimeException("Передан сенсор неизвестного типа " + sensorType);
        }

        Integer value = sensorHandlers.get(sensorType).getSensorValue(condition.getType(), sensorState);
        if (value == null)
            return false;

        boolean isCorrect = false;
        switch (condition.getOperation()) {
            case ConditionOperator.LOWER_THAN:
                isCorrect = value < condition.getValue();
                break;
            case ConditionOperator.EQUALS:
                isCorrect = value.equals(condition.getValue());
                break;
            case ConditionOperator.GREATER_THAN:
                isCorrect = value > condition.getValue();
                break;
        }
        return isCorrect;
    }

}
