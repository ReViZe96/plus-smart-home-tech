package ru.yandex.practicum.handlers.hubs;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.handlers.EventHandler;
import ru.yandex.practicum.mapper.HubAvroMapper;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;

@Service
@RequiredArgsConstructor
public abstract class HubEventHandler extends EventHandler {

    public final HubAvroMapper hubAvroMapper;

    public abstract HubEventProto.PayloadCase getMessageType();

    public abstract void handle(HubEventProto event);

}
