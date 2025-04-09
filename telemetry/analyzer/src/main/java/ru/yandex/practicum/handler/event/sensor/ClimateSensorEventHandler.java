package ru.yandex.practicum.handler.event.sensor;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.ClimateSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.model.ConditionType;

@Service
public class ClimateSensorEventHandler implements SensorEventHandler {

    @Override
    public String getSensorType() {
        return ClimateSensorAvro.class.getName();
    }

    @Override
    public Integer getSensorValue(ConditionType conditionType, SensorStateAvro sensorState) {

        ClimateSensorAvro climateSensor = (ClimateSensorAvro) sensorState.getData();

        Integer value = null;
        switch (conditionType) {
            case TEMPERATURE:
                value = climateSensor.getTemperatureC();
                break;
            case CO2LEVEL:
                value = climateSensor.getCo2Level();
                break;
            case HUMIDITY:
                value = climateSensor.getHumidity();
                break;
        }
        return value;
    }

}
