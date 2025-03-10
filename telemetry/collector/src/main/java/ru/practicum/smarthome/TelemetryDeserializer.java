package ru.practicum.smarthome;

import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

public class TelemetryDeserializer implements Deserializer<SpecificRecordBase> {

    private final DecoderFactory decoderFactory = DecoderFactory.get();


    @Override
    public SpecificRecordBase deserialize(String topic, byte[] bytes) {
        try {
            if (bytes != null) {
                BinaryDecoder decoder = decoderFactory.binaryDecoder(bytes, null);
                DatumReader<SpecificRecordBase> reader;

                switch (topic) {
                    case TelemetryTopics.TELEMETRY_SENSORS_V1:
                        reader = new SpecificDatumReader<>(SensorEventAvro.getClassSchema());
                        break;
                    case TelemetryTopics.TELEMETRY_HUBS_V1:
                        reader = new SpecificDatumReader<>(HubEventAvro.getClassSchema());
                        break;
                    default:
                        throw new IllegalArgumentException("Неизвестный топик: " + topic);
                }

                return reader.read(null, decoder);
            }
            return null;
        } catch (Exception e) {
            throw new SerializationException("Ошибка десереализации данных из топика [" + topic + "]", e);
        }
    }

}