package ru.practicum.smarthome.handlers.sensors;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.grpc.telemetry.event.MotionSensorProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;

import static ru.practicum.smarthome.TelemetryTopics.TELEMETRY_SENSORS_V1;

@Service
public class MotionSensorHandler extends SensorEventHandler {

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.MOTION_SENSOR_EVENT;
    }

    @Override
    public void handle(SensorEventProto event) {
        MotionSensorProto motionSensor = event.getMotionSensorEvent();
        sendToKafka(TELEMETRY_SENSORS_V1, getMessageType().name(), motionSensor.toByteArray());
    }

}
