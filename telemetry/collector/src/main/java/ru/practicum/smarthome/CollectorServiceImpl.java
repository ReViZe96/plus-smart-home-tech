package ru.practicum.smarthome;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Service;
import ru.practicum.smarthome.dto.hub.HubEvent;
import ru.practicum.smarthome.dto.sensor.SensorEvent;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

import java.util.Properties;


@Service
public class CollectorServiceImpl implements CollectorService {

    private final Producer<String, SpecificRecordBase> producer = initKafkaClient();


    @Override
    public void collectSensorEvent(SensorEvent event) {
        SensorEventAvro sensorEvent = SensorEventAvro.newBuilder()
                .setId(event.getId())
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setPayload(event)
                .build();

        ProducerRecord<String, SpecificRecordBase> producerSensorRecord = new ProducerRecord<>(TelemetryTopics.TELEMETRY_SENSORS_V1, sensorEvent);
        producer.send(producerSensorRecord);

    }

    @Override
    public void collectHubEvent(HubEvent event) {
        HubEventAvro hubEvent = HubEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setPayload(event)
                .build();

        ProducerRecord<String, SpecificRecordBase> producerHubRecord = new ProducerRecord<>(TelemetryTopics.TELEMETRY_HUBS_V1, hubEvent);
        producer.send(producerHubRecord);

    }


    private Producer<String, SpecificRecordBase> initKafkaClient() {
        Properties kafkaConfigs = new Properties();
        kafkaConfigs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        kafkaConfigs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        kafkaConfigs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "ru.practicum.smarthome.TelemetrySerializer");
        return new KafkaProducer<>(kafkaConfigs);

    }

}
