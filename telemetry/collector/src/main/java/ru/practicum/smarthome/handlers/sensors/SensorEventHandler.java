package ru.practicum.smarthome.handlers.sensors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.smarthome.handlers.EventHandler;
import ru.practicum.smarthome.mapper.SensorAvroMapper;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;

@Service
@RequiredArgsConstructor
public abstract class SensorEventHandler extends EventHandler {

    public final SensorAvroMapper sensorAvroMapper;

    public abstract SensorEventProto.PayloadCase getMessageType();

    public abstract void handle(SensorEventProto event);

}
