package ru.practicum.smarthome.mapper;

import org.springframework.stereotype.Service;
import ru.practicum.smarthome.dto.sensor.*;
import ru.yandex.practicum.kafka.telemetry.event.*;

@Service
public class SensorAvroMapper {

    public ClimateSensorAvro climatSensorToAvro(ClimatSensorEvent climatEvent) {
        return ClimateSensorAvro.newBuilder()
                .setTemperatureC(climatEvent.getTemperatureC())
                .setHumidity(climatEvent.getHumidity())
                .setCo2Level(climatEvent.getCo2Level())
                .build();
    }

    public LightSensorAvro lightSensorToAvro(LightSensorEvent lightEvent) {
        return LightSensorAvro.newBuilder()
                .setLinkQuality(lightEvent.getLinkQuality())
                .setLuminosity(lightEvent.getLuminosity())
                .build();
    }

    public MotionSensorAvro motionSensorToAvro(MotionSensorEvent motionEvent) {
        return MotionSensorAvro.newBuilder()
                .setLinkQuality(motionEvent.getLinkQuality())
                .setMotion(motionEvent.isMotion())
                .setVoltage(motionEvent.getVoltage())
                .build();
    }

    public SwitchSensorAvro switchSensorToAvro(SwitchSensorEvent switchEvent) {
        return SwitchSensorAvro.newBuilder()
                .setState(switchEvent.isState())
                .build();
    }

    public TemperatureSensorAvro temperatureSensorToAvro(TemperatureSensorEvent temperatureEvent) {
        return TemperatureSensorAvro.newBuilder()
                .setId(temperatureEvent.getId())
                .setHubId(temperatureEvent.getHubId())
                .setTimestamp(temperatureEvent.getTimestamp())
                .setTemperatureC(temperatureEvent.getTemperatureC())
                .setTemperatureF(temperatureEvent.getTemperatureF())
                .build();
    }

}
