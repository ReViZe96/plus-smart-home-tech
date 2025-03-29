package ru.yandex.practicum.handler.event.sensor;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.TemperatureSensorAvro;
import ru.yandex.practicum.model.ConditionType;

import java.util.Objects;

@Service
public class TemperatureEventHandler implements SensorEventHandler {

    @Override
    public String getSensorType() {
        return TemperatureSensorAvro.class.getName();
    }

    @Override
    public Integer getSensorValue(ConditionType conditionType, SensorStateAvro sensorState) {
        TemperatureSensorAvro temperatureSensor = (TemperatureSensorAvro) sensorState.getData();
        Integer value = null;
        if (Objects.requireNonNull(conditionType) == ConditionType.TEMPERATURE) {
            value = temperatureSensor.getTemperatureC();
        }
        return value;
    }

}
