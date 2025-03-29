package ru.yandex.practicum.handler.event.sensor;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.SwitchSensorAvro;
import ru.yandex.practicum.model.ConditionType;

import java.util.Objects;

@Service
public class SwitchEventHandler implements SensorEventHandler {

    @Override
    public String getSensorType() {
        return SwitchSensorAvro.class.getName();
    }

    @Override
    public Integer getSensorValue(ConditionType conditionType, SensorStateAvro sensorState) {
        SwitchSensorAvro switchSensor = (SwitchSensorAvro) sensorState.getData();
        Integer value = null;
        if (Objects.requireNonNull(conditionType) == ConditionType.SWITCH) {
            value = switchSensor.getState() ? 1 : 0;
        }
        return value;
    }

}
