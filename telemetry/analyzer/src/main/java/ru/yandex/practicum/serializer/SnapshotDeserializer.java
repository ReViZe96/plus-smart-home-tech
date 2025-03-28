package ru.yandex.practicum.serializer;

import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

public class SnapshotDeserializer extends BaseAvroDeserializer<SensorsSnapshotAvro> {

    public SnapshotDeserializer() {
        super(SensorsSnapshotAvro.getClassSchema());
    }

}
