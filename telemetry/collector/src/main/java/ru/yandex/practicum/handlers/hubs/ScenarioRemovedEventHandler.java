package ru.yandex.practicum.handlers.hubs;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.mapper.HubAvroMapper;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.ScenarioRemovedEventProto;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioRemovedEventAvro;

import java.time.Instant;

import static ru.yandex.practicum.serializer.CollectorTopics.TELEMETRY_HUBS_V1;

@Component
public class ScenarioRemovedEventHandler extends HubEventHandler {

    public ScenarioRemovedEventHandler(HubAvroMapper hubAvroMapper) {
        super(hubAvroMapper);
    }

    @Override
    public HubEventProto.PayloadCase getMessageType() {
        return HubEventProto.PayloadCase.SCENARIO_REMOVED;
    }

    @Override
    public void handle(HubEventProto event) {
        ScenarioRemovedEventProto scenarioRemovedEventProto = event.getScenarioRemoved();
        ScenarioRemovedEventAvro scenarioRemovedEventAvro = hubAvroMapper.scenarioRemovedToAvro(scenarioRemovedEventProto);
        HubEventAvro eventAvro = HubEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(Instant.ofEpochMilli((event.getTimestampOrBuilder().getNanos()) / 1000000))
                .setPayload(scenarioRemovedEventAvro)
                .build();
        sendToKafka(TELEMETRY_HUBS_V1, getMessageType().name(), eventAvro);
    }

}
