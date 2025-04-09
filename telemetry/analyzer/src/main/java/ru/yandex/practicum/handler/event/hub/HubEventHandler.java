package ru.yandex.practicum.handler.event.hub;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

@Service
public interface HubEventHandler {

    String getEventType();

    void handle(HubEventAvro hubEvent);

}
