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

        List<Scenario> scenariosWithCorrectConditions = scenarioRepository.findByHubId(snapshot.getHubId());
        Map<String, SensorStateAvro> snapshotSensorsStates = snapshot.getSensorsState();

        scenariosWithCorrectConditions = scenariosWithCorrectConditions.stream()
                .filter(scenario -> checkConditions(scenario.getConditions(), snapshotSensorsStates))
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

    private boolean checkConditions(List<Condition> conditions, Map<String, SensorStateAvro> sensorStates) {

        if (conditions == null || conditions.isEmpty()) {
            return true;
        }
        if (sensorStates == null || sensorStates.isEmpty()) {
            return false;
        }
        return conditions.stream()
                .allMatch(condition -> checkCondition(condition, sensorStates.get(condition.getSensor().getId())));

    }

    private boolean checkCondition(Condition condition, SensorStateAvro sensorState) {
        if (condition == null) {
            return false;
        }
        if (sensorState == null) {
            return false;
        }
        if (sensorState.getData() == null) {
            return false;
        }

        String type = sensorState.getData().getClass().getName();
        if (!sensorHandlers.containsKey(type)) {
            throw new IllegalArgumentException("Не найден обработчик для сенсора: " + type);
        }

        Integer value = sensorHandlers.get(type).getSensorValue(condition.getType(), sensorState);

        if (value == null) {
            return false;
        }

        return switch (condition.getOperation()) {
            case ConditionOperator.LOWER_THAN -> value < condition.getValue();
            case ConditionOperator.EQUALS -> value.equals(condition.getValue());
            case ConditionOperator.GREATER_THAN -> value > condition.getValue();
        };

    }
}