package ru.yandex.practicum.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AggregatorService {

    Map<String, SensorsSnapshotAvro> allSnapshots = new HashMap<>();

    public Optional<SensorsSnapshotAvro> updateState(SensorEventAvro sensorEvent) {

        SensorsSnapshotAvro newSnapshot;
        SensorsSnapshotAvro existedSnapshot;
        String hubId = sensorEvent.getHubId();
        if (!allSnapshots.containsKey(hubId)) {
            newSnapshot = SensorsSnapshotAvro.newBuilder()
                    .setHubId(hubId)
                    .setTimestamp(Instant.now())
                    .setSensorsState(new HashMap<>())
                    .build();
            allSnapshots.put(hubId, newSnapshot);
            return Optional.of(newSnapshot);
        } else {
            existedSnapshot = allSnapshots.get(hubId);
            SensorStateAvro oldSensorState = existedSnapshot.getSensorsState().get(sensorEvent.getId());
            if (oldSensorState != null && (oldSensorState.getTimestamp().isAfter(sensorEvent.getTimestamp())
                    || oldSensorState.getData().equals(sensorEvent.getPayload()))) {
                return Optional.empty();
            } else {
                SensorStateAvro newSensorState = SensorStateAvro.newBuilder()
                        .setTimestamp(sensorEvent.getTimestamp())
                        .setData(sensorEvent.getPayload())
                        .build();
                existedSnapshot.getSensorsState().put(sensorEvent.getId(), newSensorState);
                existedSnapshot.setTimestamp(sensorEvent.getTimestamp());
                existedSnapshot.put(hubId, existedSnapshot);
                return Optional.of(existedSnapshot);
            }
        }
    }

}