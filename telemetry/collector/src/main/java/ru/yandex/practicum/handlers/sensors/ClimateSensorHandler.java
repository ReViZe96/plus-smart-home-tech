package ru.yandex.practicum.handlers.sensors;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.mapper.SensorAvroMapper;
import ru.yandex.practicum.grpc.telemetry.event.ClimateSensorProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.ClimateSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

import java.time.Instant;

import static ru.yandex.practicum.serializer.CollectorTopics.TELEMETRY_SENSORS_V1;

@Component
public class ClimateSensorHandler extends SensorEventHandler {

    public ClimateSensorHandler(SensorAvroMapper sensorAvroMapper) {
        super(sensorAvroMapper);
    }

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.CLIMATE_SENSOR_EVENT;
    }

    @Override
    public void handle(SensorEventProto event) {
        ClimateSensorProto climateSensorProto = event.getClimateSensorEvent();
        ClimateSensorAvro climateSensorAvro = sensorAvroMapper.climatSensorToAvro(climateSensorProto);
        SensorEventAvro eventAvro = SensorEventAvro.newBuilder()
                .setId(event.getId())
                .setHubId(event.getHubId())
                .setTimestamp(Instant.ofEpochSecond(event.getTimestampOrBuilder().getSeconds(),
                        event.getTimestampOrBuilder().getNanos()))
                .setPayload(climateSensorAvro)
                .build();
        eventAvro.setPayload(climateSensorAvro);
        sendToKafka(TELEMETRY_SENSORS_V1, getMessageType().name(), eventAvro);
    }

}
