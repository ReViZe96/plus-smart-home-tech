package ru.practicum.smarthome.dto.hub;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.yandex.practicum.kafka.telemetry.event.ConditionOperationAvro;
import ru.yandex.practicum.kafka.telemetry.event.ConditionTypeAvro;

@Getter
@Setter
@ToString(callSuper = true)
public class ScenarioCondition {

    private String sensorId;
    private ConditionTypeAvro type;
    private ConditionOperationAvro operation;
    private Boolean value;

}
