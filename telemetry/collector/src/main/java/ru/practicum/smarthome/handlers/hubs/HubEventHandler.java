package ru.practicum.smarthome.handlers.hubs;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.smarthome.handlers.EventHandler;
import ru.practicum.smarthome.mapper.HubAvroMapper;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;

@Service
@RequiredArgsConstructor
public abstract class HubEventHandler extends EventHandler {

    public final HubAvroMapper hubAvroMapper;

    public abstract HubEventProto.PayloadCase getMessageType();

    public abstract void handle(HubEventProto event);

}
