package ru.yandex.practicum.handler.event.hub;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.model.Sensor;
import ru.yandex.practicum.repository.SensorRepository;

@Service
@RequiredArgsConstructor
public class DeviceAddedEventHandler implements HubEventHandler {

    private final SensorRepository sensorRepository;

    @Override
    public String getEventType() {
        return DeviceAddedEventAvro.class.getName();
    }

    @Override
    public void handle(HubEventAvro hubEvent) {
        DeviceAddedEventAvro deviceAddedEvent = (DeviceAddedEventAvro) hubEvent.getPayload();
        Sensor sensor = new Sensor(deviceAddedEvent.getId(), hubEvent.getHubId());
        sensorRepository.save(sensor);
    }

}
