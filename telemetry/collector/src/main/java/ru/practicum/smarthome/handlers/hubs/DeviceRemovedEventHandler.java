package ru.practicum.smarthome.handlers.hubs;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.DeviceRemovedEventProto;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;

import static ru.practicum.smarthome.TelemetryTopics.TELEMETRY_HUBS_V1;

@Component
public class DeviceRemovedEventHandler extends HubEventHandler {

    @Override
    public HubEventProto.PayloadCase getMessageType() {
        return HubEventProto.PayloadCase.DEVICE_REMOVED;
    }

    @Override
    public void handle(HubEventProto event) {
        DeviceRemovedEventProto deviceRemovedEvent = event.getDeviceRemoved();
        sendToKafka(TELEMETRY_HUBS_V1, getMessageType().name(), deviceRemovedEvent.toByteArray());
    }

}
