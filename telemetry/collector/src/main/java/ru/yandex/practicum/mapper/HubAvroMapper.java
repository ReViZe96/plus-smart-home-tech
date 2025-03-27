package ru.yandex.practicum.mapper;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.grpc.telemetry.event.*;
import ru.yandex.practicum.kafka.telemetry.event.*;

import java.util.List;

@Service
public class HubAvroMapper {

    public DeviceAddedEventAvro deviceAddedToAvro(DeviceAddedEventProto deviceAddedEvent) {
        return DeviceAddedEventAvro.newBuilder()
                .setId(deviceAddedEvent.getId())
                .setType(DeviceTypeAvro.valueOf(deviceAddedEvent.getType().name()))
                .build();
    }

    public DeviceRemovedEventAvro deviceRemovedToAvro(DeviceRemovedEventProto deviceRemovedEvent) {
        return DeviceRemovedEventAvro.newBuilder()
                .setId(deviceRemovedEvent.getId())
                .build();
    }

    public ScenarioAddedEventAvro scenarioAddedToAvro(ScenarioAddedEventProto scenarioAddedEvent) {
        List<ScenarioConditionAvro> scenarioConditionsAvro = scenarioAddedEvent.getConditionList().stream()
                .map(this::scenarioConditionToAvro)
                .toList();
        List<DeviceActionAvro> deviceActionsAvro = scenarioAddedEvent.getActionList().stream()
                .map(this::deviceActionToAvro)
                .toList();
        return ScenarioAddedEventAvro.newBuilder()
                .setName(scenarioAddedEvent.getName())
                .setConditions(scenarioConditionsAvro)
                .setActions(deviceActionsAvro)
                .build();
    }

    public ScenarioRemovedEventAvro scenarioRemovedToAvro(ScenarioRemovedEventProto scenarioRemovedEvent) {
        return ScenarioRemovedEventAvro.newBuilder()
                .setName(scenarioRemovedEvent.getName())
                .build();
    }


    private ScenarioConditionAvro scenarioConditionToAvro(ScenarioConditionProto scenarioCondition) {
        return ScenarioConditionAvro.newBuilder()
                .setSensorId(scenarioCondition.getSensorId())
                .setType(ConditionTypeAvro.valueOf(scenarioCondition.getType().name()))
                .setOperation(ConditionOperationAvro.valueOf(scenarioCondition.getOperation().name()))
                .setValue(scenarioCondition.hasIntValue() ? scenarioCondition.getIntValue() : scenarioCondition.getBoolValue())
                .build();
    }

    private DeviceActionAvro deviceActionToAvro(DeviceActionProto deviceAction) {
        return DeviceActionAvro.newBuilder()
                .setSensorId(deviceAction.getSensorId())
                .setType(ActionTypeAvro.valueOf(deviceAction.getType().name()))
                .setValue(deviceAction.getValue())
                .build();
    }

}