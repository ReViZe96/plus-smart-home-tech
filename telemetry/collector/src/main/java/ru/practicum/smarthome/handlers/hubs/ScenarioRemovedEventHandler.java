package ru.practicum.smarthome.handlers.hubs;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.ScenarioRemovedEventProto;

import static ru.practicum.smarthome.TelemetryTopics.TELEMETRY_HUBS_V1;

@Component
public class ScenarioRemovedEventHandler extends HubEventHandler {

    @Override
    public HubEventProto.PayloadCase getMessageType() {
        return HubEventProto.PayloadCase.SCENARIO_REMOVED;
    }

    @Override
    public void handle(HubEventProto event) {
        ScenarioRemovedEventProto scenarioRemovedEvent = event.getScenarioRemoved();
        sendToKafka(TELEMETRY_HUBS_V1, getMessageType().name(), scenarioRemovedEvent.toByteArray());
    }

}
