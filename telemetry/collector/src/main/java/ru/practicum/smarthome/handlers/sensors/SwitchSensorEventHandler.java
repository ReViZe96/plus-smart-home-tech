package ru.practicum.smarthome.handlers.sensors;

import org.springframework.stereotype.Component;
import ru.practicum.smarthome.mapper.SensorAvroMapper;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.grpc.telemetry.event.SwitchSensorProto;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SwitchSensorAvro;

import java.time.Instant;

import static ru.practicum.smarthome.TelemetryTopics.TELEMETRY_SENSORS_V1;

@Component
public class SwitchSensorEventHandler extends SensorEventHandler {

    public SwitchSensorEventHandler(SensorAvroMapper sensorAvroMapper) {
        super(sensorAvroMapper);
    }

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.SWITCH_SENSOR_EVENT;
    }

    @Override
    public void handle(SensorEventProto event) {
        SwitchSensorProto switchSensorProto = event.getSwitchSensorEvent();
        SwitchSensorAvro switchSensorAvro = sensorAvroMapper.switchSensorToAvro(switchSensorProto);
        SensorEventAvro eventAvro = SensorEventAvro.newBuilder()
                .setId(event.getId())
                .setHubId(event.getHubId())
                .setTimestamp(Instant.ofEpochSecond(event.getTimestampOrBuilder().getSeconds()))
                .setPayload(switchSensorAvro)
                .build();
        sendToKafka(TELEMETRY_SENSORS_V1, getMessageType().name(), eventAvro);
    }

}
