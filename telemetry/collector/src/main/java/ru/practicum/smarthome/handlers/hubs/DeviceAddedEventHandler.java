package ru.practicum.smarthome.handlers.hubs;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.DeviceAddedEventProto;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;

import static ru.practicum.smarthome.TelemetryTopics.TELEMETRY_HUBS_V1;

@Component
public class DeviceAddedEventHandler extends HubEventHandler {

    @Override
    public HubEventProto.PayloadCase getMessageType() {
        return HubEventProto.PayloadCase.DEVICE_ADDED;
    }

    @Override
    public void handle(HubEventProto event) {
        DeviceAddedEventProto deviceAddedEvent = event.getDeviceAdded();
        sendToKafka(TELEMETRY_HUBS_V1, getMessageType().name(), deviceAddedEvent.toByteArray());
    }

}
