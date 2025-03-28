package ru.yandex.practicum.handler.event.sensor;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.LightSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.model.ConditionType;

import java.util.Objects;

@Service
public class LightEventHandler implements SensorEventHandler {

    @Override
    public String getSensorType() {
        return LightSensorAvro.class.getName();
    }

    @Override
    public Integer getSensorValue(ConditionType conditionType, SensorStateAvro sensorState) {
        LightSensorAvro lightSensor = (LightSensorAvro) sensorState.getData();
        Integer value = null;
        if (Objects.requireNonNull(conditionType) == ConditionType.LUMINOSITY) {
            value = lightSensor.getLuminosity();
        }
        return value;
    }

}
