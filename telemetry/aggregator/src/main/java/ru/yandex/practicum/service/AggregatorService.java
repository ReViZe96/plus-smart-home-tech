package ru.yandex.practicum.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class AggregatorService {

    Map<String, SensorsSnapshotAvro> allSnapshots = new HashMap<>();

    public Optional<SensorsSnapshotAvro> updateState(SensorEventAvro sensorEvent) {

        SensorsSnapshotAvro newSnapshot;
        SensorsSnapshotAvro existedSnapshot;
        String hubId = sensorEvent.getHubId();
        if (!allSnapshots.containsKey(hubId)) {
            log.info("Событие с id ={}, является первым в рамках хаба {}. Добавление данных в снапшот.",
                    sensorEvent.getId(), hubId);
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
                log.info("Полученное событие (id = {}) произошло раньше, чем последнее обновление данных датчика в рамках хаба. " +
                                "Данные из него будут проигнорированы",
                        sensorEvent.getId());
                return Optional.empty();
            } else {
                log.info("Получено новое событие с id = {}. Обновление данных по хабу {}.", sensorEvent.getId(),
                        existedSnapshot.getHubId());

                SensorStateAvro newSensorState = SensorStateAvro.newBuilder()
                        .setTimestamp(sensorEvent.getTimestamp())
                        .setData(sensorEvent.getPayload())
                        .build();
                existedSnapshot.getSensorsState().put(sensorEvent.getId(), newSensorState);
                existedSnapshot.setTimestamp(sensorEvent.getTimestamp());
                allSnapshots.put(hubId, existedSnapshot);
                return Optional.of(existedSnapshot);
            }
        }
    }

}