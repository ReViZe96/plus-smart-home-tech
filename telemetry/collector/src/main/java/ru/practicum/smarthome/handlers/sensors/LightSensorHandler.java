package ru.practicum.smarthome.handlers.sensors;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.grpc.telemetry.event.LightSensorProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;

import static ru.practicum.smarthome.TelemetryTopics.TELEMETRY_SENSORS_V1;

@Service
public class LightSensorHandler extends SensorEventHandler {

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.LIGHT_SENSOR_EVENT;
    }

    @Override
    public void handle(SensorEventProto event) {
        LightSensorProto lightSensor = event.getLightSensorEvent();
        sendToKafka(TELEMETRY_SENSORS_V1, getMessageType().name(), lightSensor.toByteArray());
    }

}
