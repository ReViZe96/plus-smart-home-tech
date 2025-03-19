package ru.practicum.smarthome.handlers.hubs;

import org.springframework.stereotype.Component;
import ru.practicum.smarthome.mapper.HubAvroMapper;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.ScenarioRemovedEventProto;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioRemovedEventAvro;

import java.time.Instant;

import static ru.practicum.smarthome.TelemetryTopics.TELEMETRY_HUBS_V1;

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
                .setTimestamp(Instant.ofEpochSecond(event.getTimestampOrBuilder().getSeconds()))
                .setPayload(scenarioRemovedEventAvro)
                .build();
        sendToKafka(TELEMETRY_HUBS_V1, getMessageType().name(), eventAvro);
    }

}
