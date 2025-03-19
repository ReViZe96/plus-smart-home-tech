package ru.practicum.smarthome.handlers.hubs;

import org.springframework.stereotype.Service;
import ru.practicum.smarthome.handlers.EventHandler;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;

@Service
public abstract class HubEventHandler extends EventHandler {

    public abstract HubEventProto.PayloadCase getMessageType();

    public abstract void handle(HubEventProto event);

}
