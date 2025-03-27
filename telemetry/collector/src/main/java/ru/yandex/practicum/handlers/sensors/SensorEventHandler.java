package ru.yandex.practicum.handlers.sensors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.handlers.EventHandler;
import ru.yandex.practicum.mapper.SensorAvroMapper;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;

@Service
@RequiredArgsConstructor
public abstract class SensorEventHandler extends EventHandler {

    public final SensorAvroMapper sensorAvroMapper;

    public abstract SensorEventProto.PayloadCase getMessageType();

    public abstract void handle(SensorEventProto event);

}
