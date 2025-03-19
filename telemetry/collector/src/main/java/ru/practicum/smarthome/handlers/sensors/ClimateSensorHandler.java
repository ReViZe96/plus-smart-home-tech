package ru.practicum.smarthome.handlers.sensors;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.ClimateSensorProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;

import static ru.practicum.smarthome.TelemetryTopics.TELEMETRY_SENSORS_V1;

@Component
public class ClimateSensorHandler extends SensorEventHandler {

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.CLIMATE_SENSOR_EVENT;
    }

    @Override
    public void handle(SensorEventProto event) {
        ClimateSensorProto climateSensor = event.getClimateSensorEvent();
        sendToKafka(TELEMETRY_SENSORS_V1, getMessageType().name(), climateSensor.toByteArray());
    }

}
