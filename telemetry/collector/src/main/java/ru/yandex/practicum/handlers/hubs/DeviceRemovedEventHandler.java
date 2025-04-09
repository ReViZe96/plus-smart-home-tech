package ru.yandex.practicum.handlers.hubs;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.mapper.HubAvroMapper;
import ru.yandex.practicum.grpc.telemetry.event.DeviceRemovedEventProto;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.telemetry.event.DeviceRemovedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

import java.time.Instant;

import static ru.yandex.practicum.serializer.CollectorTopics.TELEMETRY_HUBS_V1;

@Component
public class DeviceRemovedEventHandler extends HubEventHandler {

    public DeviceRemovedEventHandler(HubAvroMapper hubAvroMapper) {
        super(hubAvroMapper);
    }

    @Override
    public HubEventProto.PayloadCase getMessageType() {
        return HubEventProto.PayloadCase.DEVICE_REMOVED;
    }

    @Override
    public void handle(HubEventProto event) {
        DeviceRemovedEventProto deviceRemovedEventProto = event.getDeviceRemoved();
        DeviceRemovedEventAvro deviceRemovedEventAvro = hubAvroMapper.deviceRemovedToAvro(deviceRemovedEventProto);
        HubEventAvro eventAvro = HubEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(Instant.ofEpochSecond(event.getTimestampOrBuilder().getSeconds(),
                        event.getTimestampOrBuilder().getNanos()))
                .setPayload(deviceRemovedEventAvro)
                .build();
        sendToKafka(TELEMETRY_HUBS_V1, getMessageType().name(), eventAvro);
    }

}
