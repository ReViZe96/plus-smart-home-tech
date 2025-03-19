package ru.practicum.smarthome.handlers.sensors;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.grpc.telemetry.event.SwitchSensorProto;

import static ru.practicum.smarthome.TelemetryTopics.TELEMETRY_SENSORS_V1;

@Component
public class SwitchSensorEventHandler extends SensorEventHandler {

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.SWITCH_SENSOR_EVENT;
    }

    @Override
    public void handle(SensorEventProto event) {
        SwitchSensorProto switchSensor = event.getSwitchSensorEvent();
        sendToKafka(TELEMETRY_SENSORS_V1, getMessageType().name(), switchSensor.toByteArray());
    }

}
