package ru.practicum.smarthome.handlers.sensors;

import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.grpc.telemetry.event.TemperatureSensorProto;

import static ru.practicum.smarthome.TelemetryTopics.TELEMETRY_SENSORS_V1;

public class TemperatureSensorHandler extends SensorEventHandler {

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.TEMPERATURE_SENSOR_EVENT;
    }

    @Override
    public void handle(SensorEventProto event) {
        TemperatureSensorProto temperatureSensor = event.getTemperatureSensorEvent();
        sendToKafka(TELEMETRY_SENSORS_V1, getMessageType().name(), temperatureSensor.toByteArray());
    }

}
