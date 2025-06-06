package ru.yandex.practicum.handlers.hubs;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.mapper.HubAvroMapper;
import ru.yandex.practicum.grpc.telemetry.event.DeviceAddedEventProto;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

import java.time.Instant;

import static ru.yandex.practicum.serializer.CollectorTopics.TELEMETRY_HUBS_V1;

@Component
public class DeviceAddedEventHandler extends HubEventHandler {


    public DeviceAddedEventHandler(HubAvroMapper hubAvroMapper) {
        super(hubAvroMapper);
    }

    @Override
    public HubEventProto.PayloadCase getMessageType() {
        return HubEventProto.PayloadCase.DEVICE_ADDED;
    }

    @Override
    public void handle(HubEventProto event) {
        DeviceAddedEventProto deviceAddedEventProto = event.getDeviceAdded();
        DeviceAddedEventAvro deviceAddedEventAvro = hubAvroMapper.deviceAddedToAvro(deviceAddedEventProto);
        HubEventAvro eventAvro = HubEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(Instant.ofEpochSecond(event.getTimestampOrBuilder().getSeconds(),
                        event.getTimestampOrBuilder().getNanos()))
                .setPayload(deviceAddedEventAvro)
                .build();
        sendToKafka(TELEMETRY_HUBS_V1, getMessageType().name(), eventAvro);
    }

}
