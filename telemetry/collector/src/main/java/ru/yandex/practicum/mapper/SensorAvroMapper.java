package ru.yandex.practicum.mapper;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.grpc.telemetry.event.*;
import ru.yandex.practicum.kafka.telemetry.event.*;

@Service
public class SensorAvroMapper {

    public ClimateSensorAvro climatSensorToAvro(ClimateSensorProto climatEvent) {
        return ClimateSensorAvro.newBuilder()
                .setTemperatureC(climatEvent.getTemperatureC())
                .setHumidity(climatEvent.getHumidity())
                .setCo2Level(climatEvent.getCo2Level())
                .build();
    }

    public LightSensorAvro lightSensorToAvro(LightSensorProto lightEvent) {
        return LightSensorAvro.newBuilder()
                .setLinkQuality(lightEvent.getLinkQuality())
                .setLuminosity(lightEvent.getLuminosity())
                .build();
    }

    public MotionSensorAvro motionSensorToAvro(MotionSensorProto motionEvent) {
        return MotionSensorAvro.newBuilder()
                .setLinkQuality(motionEvent.getLinkQuality())
                .setMotion(motionEvent.getMotion())
                .setVoltage(motionEvent.getVoltage())
                .build();
    }

    public SwitchSensorAvro switchSensorToAvro(SwitchSensorProto switchEvent) {
        return SwitchSensorAvro.newBuilder()
                .setState(switchEvent.getState())
                .build();
    }

    public TemperatureSensorAvro temperatureSensorToAvro(TemperatureSensorProto temperatureEvent) {
        return TemperatureSensorAvro.newBuilder()
                .setTemperatureC(temperatureEvent.getTemperatureC())
                .setTemperatureF(temperatureEvent.getTemperatureF())
                .build();
    }

}