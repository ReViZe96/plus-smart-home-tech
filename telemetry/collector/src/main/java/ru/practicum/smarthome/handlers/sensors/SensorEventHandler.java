package ru.practicum.smarthome.handlers.sensors;

import org.springframework.stereotype.Service;
import ru.practicum.smarthome.handlers.EventHandler;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;

@Service
public abstract class SensorEventHandler extends EventHandler {

    public abstract SensorEventProto.PayloadCase getMessageType();

    public abstract void handle(SensorEventProto event);

}
