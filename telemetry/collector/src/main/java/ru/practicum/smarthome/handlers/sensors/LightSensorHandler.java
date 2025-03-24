package ru.practicum.smarthome.handlers.sensors;

import org.springframework.stereotype.Component;
import ru.practicum.smarthome.mapper.SensorAvroMapper;
import ru.yandex.practicum.grpc.telemetry.event.LightSensorProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.LightSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

import java.time.Instant;

import static ru.practicum.smarthome.TelemetryTopics.TELEMETRY_SENSORS_V1;

@Component
public class LightSensorHandler extends SensorEventHandler {

    public LightSensorHandler(SensorAvroMapper sensorAvroMapper) {
        super(sensorAvroMapper);
    }

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.LIGHT_SENSOR_EVENT;
    }

    @Override
    public void handle(SensorEventProto event) {
        LightSensorProto lightSensorProto = event.getLightSensorEvent();
        LightSensorAvro lightSensorAvro = sensorAvroMapper.lightSensorToAvro(lightSensorProto);
        SensorEventAvro eventAvro = SensorEventAvro.newBuilder()
                .setId(event.getId())
                .setHubId(event.getHubId())
                .setTimestamp(Instant.ofEpochSecond(event.getTimestampOrBuilder().getSeconds(),
                        event.getTimestampOrBuilder().getNanos()))
                .setPayload(lightSensorAvro)
                .build();
        sendToKafka(TELEMETRY_SENSORS_V1, getMessageType().name(), eventAvro);
    }

}
