package ru.practicum.smarthome.mapper;

import org.springframework.stereotype.Service;
import ru.practicum.smarthome.dto.hub.*;
import ru.yandex.practicum.kafka.telemetry.event.*;

import java.util.List;

@Service
public class HubAvroMapper {

    public DeviceAddedEventAvro deviceAddedToAvro(DeviceAddedEvent deviceAddedEvent) {
        return DeviceAddedEventAvro.newBuilder()
                .setId(deviceAddedEvent.getId())
                .setType(deviceAddedEvent.getDeviceType())
                .build();
    }

    public DeviceRemovedEventAvro deviceRemovedToAvro(DeviceRemovedEvent deviceRemovedEvent) {
        return DeviceRemovedEventAvro.newBuilder()
                .setId(deviceRemovedEvent.getId())
                .build();
    }

    public ScenarioAddedEventAvro scenarioAddedToAvro(ScenarioAddedEvent scenarioAddedEvent) {
        List<ScenarioConditionAvro> scenarioConditionsAvro = scenarioAddedEvent.getConditions().stream()
                .map(this::scenarioConditionToAvro)
                .toList();
        List<DeviceActionAvro> deviceActionsAvro = scenarioAddedEvent.getActions().stream()
                .map(this::deviceActionToAvro)
                .toList();
        return ScenarioAddedEventAvro.newBuilder()
                .setName(scenarioAddedEvent.getName())
                .setConditions(scenarioConditionsAvro)
                .setActions(deviceActionsAvro)
                .build();
    }

    public ScenarioRemovedEventAvro scenarioRemovedToAvro(ScenarioRemovedEvent scenarioRemovedEvent) {
        return ScenarioRemovedEventAvro.newBuilder()
                .setName(scenarioRemovedEvent.getName())
                .build();
    }


    private ScenarioConditionAvro scenarioConditionToAvro(ScenarioCondition scenarioCondition) {
        return ScenarioConditionAvro.newBuilder()
                .setSensorId(scenarioCondition.getSensorId())
                .setType(scenarioCondition.getType())
                .setOperation(scenarioCondition.getOperation())
                .setValue(scenarioCondition.getValue())
                .build();
    }

    private DeviceActionAvro deviceActionToAvro(DeviceAction deviceAction) {
        return DeviceActionAvro.newBuilder()
                .setSensorId(deviceAction.getSensorId())
                .setType(deviceAction.getType())
                .setValue(deviceAction.getValue())
                .build();
    }

}
