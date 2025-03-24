package ru.practicum.smarthome.handlers.hubs;

import org.springframework.stereotype.Component;
import ru.practicum.smarthome.mapper.HubAvroMapper;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.ScenarioAddedEventProto;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioAddedEventAvro;

import java.time.Instant;

import static ru.practicum.smarthome.TelemetryTopics.TELEMETRY_HUBS_V1;

@Component
public class ScenarioAddedEventHandler extends HubEventHandler {

    public ScenarioAddedEventHandler(HubAvroMapper hubAvroMapper) {
        super(hubAvroMapper);
    }

    @Override
    public HubEventProto.PayloadCase getMessageType() {
        return HubEventProto.PayloadCase.SCENARIO_ADDED;
    }

    @Override
    public void handle(HubEventProto event) {
        ScenarioAddedEventProto scenarioAddedEventProto = event.getScenarioAdded();
        ScenarioAddedEventAvro scenarioAddedEventAvro = hubAvroMapper.scenarioAddedToAvro(scenarioAddedEventProto);
        HubEventAvro eventAvro = HubEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(Instant.ofEpochSecond(event.getTimestampOrBuilder().getSeconds(),
                        event.getTimestampOrBuilder().getNanos()))
                .setPayload(scenarioAddedEventAvro)
                .build();
        sendToKafka(TELEMETRY_HUBS_V1, getMessageType().name(), eventAvro);
    }

}
