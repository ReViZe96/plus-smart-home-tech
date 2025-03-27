package ru.yandex.practicum.handlers;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.serializer.CollectorSerializer;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Component
public class EventHandler {

    private final Producer<String, SpecificRecordBase> producer = initKafkaProducer();

    public void sendToKafka(String topicName, String eventType, SpecificRecordBase event) {
        ProducerRecord<String, SpecificRecordBase> producerSensorRecord = new ProducerRecord<>(
                topicName,
                null,
                System.currentTimeMillis(),
                eventType,
                event);
        Future<RecordMetadata> message = producer.send(producerSensorRecord);
        try {
            message.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Ошибка во время отправки сообщения в Kafka");
        }
    }

    private Producer<String, SpecificRecordBase> initKafkaProducer() {
        Properties kafkaConfigs = new Properties();
        kafkaConfigs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        kafkaConfigs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        kafkaConfigs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, CollectorSerializer.class.getName());
        kafkaConfigs.put(ProducerConfig.LINGER_MS_CONFIG, 3000);
        return new KafkaProducer<>(kafkaConfigs);
    }

}
