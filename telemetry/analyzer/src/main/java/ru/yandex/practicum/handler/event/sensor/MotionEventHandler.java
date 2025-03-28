package ru.yandex.practicum.handler.event.sensor;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.MotionSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.model.ConditionType;

import java.util.Objects;

@Service
public class MotionEventHandler implements SensorEventHandler {

    @Override
    public String getSensorType() {
        return MotionSensorAvro.class.getName();
    }

    @Override
    public Integer getSensorValue(ConditionType conditionType, SensorStateAvro sensorState) {
        MotionSensorAvro motionSensor = (MotionSensorAvro) sensorState.getData();
        Integer value = null;
        if (Objects.requireNonNull(conditionType) == ConditionType.MOTION) {
            value = motionSensor.getMotion() ? 1 : 0;
        }
        return value;
    }

}
