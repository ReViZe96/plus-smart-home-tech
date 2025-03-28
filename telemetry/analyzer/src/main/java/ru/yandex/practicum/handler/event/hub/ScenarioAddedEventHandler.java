package ru.yandex.practicum.handler.event.hub;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.kafka.telemetry.event.*;
import ru.yandex.practicum.model.*;
import ru.yandex.practicum.repository.ActionRepository;
import ru.yandex.practicum.repository.ConditionRepository;
import ru.yandex.practicum.repository.ScenarioRepository;
import ru.yandex.practicum.repository.SensorRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScenarioAddedEventHandler implements HubEventHandler {

    private final ScenarioRepository scenarioRepository;
    private final SensorRepository sensorRepository;
    private final ConditionRepository conditionRepository;
    private final ActionRepository actionRepository;

    @Override
    public String getEventType() {
        return ScenarioAddedEventAvro.class.getName();
    }

    @Transactional
    @Override
    public void handle(HubEventAvro hubEvent) {

        ScenarioAddedEventAvro scenarioEvent = (ScenarioAddedEventAvro) hubEvent.getPayload();
        boolean isSensorExists = isSensorExists(scenarioEvent.getConditions(), scenarioEvent.getActions(),
                hubEvent.getHubId());
        if (!isSensorExists) {
            throw new RuntimeException("Сенсоры не найдены");
        }

        Optional<Scenario> existedScenario = scenarioRepository.findByHubIdAndName(hubEvent.getHubId(),
                scenarioEvent.getName());

        List<Long> prevConditionIds = new ArrayList<>();
        List<Long> prevActionIds = new ArrayList<>();
        Scenario currentScenario;
        if (existedScenario.isPresent()) {
            currentScenario = existedScenario.get();
            prevConditionIds = currentScenario.getConditions().stream()
                    .map(Condition::getId).toList();
            prevActionIds = currentScenario.getActions().stream()
                    .map(Action::getId).toList();

            currentScenario.setConditions(scenarioEvent.getConditions().stream()
                    .map(conditionAvro -> mapToCondition(currentScenario, conditionAvro))
                    .collect(Collectors.toList()));
            currentScenario.setActions(scenarioEvent.getActions().stream()
                    .map(actionAvro -> mapToAction(currentScenario, actionAvro))
                    .collect(Collectors.toList()));
        } else {
            currentScenario = mapToScenario(hubEvent, scenarioEvent);
        }

        scenarioRepository.save(currentScenario);
        dropPrevConditions(prevConditionIds);
        dropPrevActions(prevActionIds);
    }

    private boolean isSensorExists(Collection<ScenarioConditionAvro> conditions,
                                   Collection<DeviceActionAvro> actions,
                                   String hubId) {
        List<String> sensorIds = getConditionSensorIds(conditions);
        sensorIds.addAll(getActionSensorIds(actions));
        List<String> notFounded = sensorIds.stream()
                .filter(sensorId -> sensorRepository.findByIdAndHubId(sensorId, hubId).isEmpty())
                .toList();
        return notFounded.isEmpty();

    }

    private void dropPrevConditions(Collection<Long> conditionIds) {
        if (!conditionIds.isEmpty()) {
            conditionRepository.deleteAllById(conditionIds);
        }
    }

    private void dropPrevActions(Collection<Long> actionIds) {
        if (!actionIds.isEmpty()) {
            actionRepository.deleteAllById(actionIds);
        }
    }

    private List<String> getConditionSensorIds(Collection<ScenarioConditionAvro> conditions) {
        return conditions.stream().map(ScenarioConditionAvro::getSensorId).toList();
    }

    private List<String> getActionSensorIds(Collection<DeviceActionAvro> actions) {
        return actions.stream().map(DeviceActionAvro::getSensorId).toList();
    }

    private Scenario mapToScenario(HubEventAvro hubEvent, ScenarioAddedEventAvro scenarioAddedEvent) {
        Scenario scenario = new Scenario();
        scenario.setHubId(hubEvent.getHubId());
        scenario.setName(scenarioAddedEvent.getName());
        scenario.setConditions(scenarioAddedEvent.getConditions()
                .stream()
                .map(conditionAvro -> mapToCondition(scenario, conditionAvro))
                .toList());
        scenario.setActions(scenarioAddedEvent.getActions()
                .stream()
                .map(actionAvro -> mapToAction(scenario, actionAvro))
                .toList());
        return scenario;
    }

    private Condition mapToCondition(Scenario scenario, ScenarioConditionAvro conditionAvro) {
        Sensor sensor = new Sensor(conditionAvro.getSensorId(), scenario.getHubId());
        return Condition.builder()
                .sensor(sensor)
                .type(ConditionType.valueOf(conditionAvro.getType().name()))
                .operation(ConditionOperator.valueOf(conditionAvro.getOperation().name()))
                .value(getConditionValue(conditionAvro.getValue()))
                .scenarios(List.of(scenario))
                .build();
    }

    private Action mapToAction(Scenario scenario, DeviceActionAvro deviceAction) {
        Sensor sensor = new Sensor(deviceAction.getSensorId(), scenario.getHubId());
        return Action.builder()
                .sensor(sensor)
                .type(ActionType.valueOf(deviceAction.getType().name()))
                .value(deviceAction.getValue())
                .build();
    }

    private Integer getConditionValue(Object value) {
        return switch (value) {
            case null -> null;
            case Integer i -> i;
            case Boolean b -> (b ? 1 : 0);
            default -> throw new ClassCastException("Передан некорректный тип значения условия");
        };
    }

}
