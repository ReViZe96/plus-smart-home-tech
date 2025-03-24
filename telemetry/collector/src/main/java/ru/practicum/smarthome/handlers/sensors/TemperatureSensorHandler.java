package ru.practicum.smarthome.handlers.sensors;

import org.springframework.stereotype.Component;
import ru.practicum.smarthome.mapper.SensorAvroMapper;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.grpc.telemetry.event.TemperatureSensorProto;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.TemperatureSensorAvro;

import java.time.Instant;

import static ru.practicum.smarthome.TelemetryTopics.TELEMETRY_SENSORS_V1;

@Component
public class TemperatureSensorHandler extends SensorEventHandler {

    public TemperatureSensorHandler(SensorAvroMapper sensorAvroMapper) {
        super(sensorAvroMapper);
    }

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.TEMPERATURE_SENSOR_EVENT;
    }

    @Override
    public void handle(SensorEventProto event) {
        TemperatureSensorProto temperatureSensorProto = event.getTemperatureSensorEvent();
        TemperatureSensorAvro temperatureSensorAvro = sensorAvroMapper.temperatureSensorToAvro(temperatureSensorProto);
        SensorEventAvro eventAvro = SensorEventAvro.newBuilder()
                .setId(event.getId())
                .setHubId(event.getHubId())
                .setTimestamp(Instant.ofEpochSecond(event.getTimestampOrBuilder().getSeconds(),
                        event.getTimestampOrBuilder().getNanos()))
                .setPayload(temperatureSensorAvro)
                .build();
        sendToKafka(TELEMETRY_SENSORS_V1, getMessageType().name(), eventAvro);
    }

}
