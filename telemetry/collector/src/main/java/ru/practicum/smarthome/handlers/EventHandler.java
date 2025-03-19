package ru.practicum.smarthome.handlers;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.BytesSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import ru.practicum.smarthome.TelemetryTopics;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class EventHandler {

    private final Producer<String, byte[]> producer = initKafkaProducer();

    public void sendToKafka(String topicName, String eventType, byte[] event) {
        ProducerRecord<String, byte[]> producerSensorRecord = new ProducerRecord<>(
                TelemetryTopics.TELEMETRY_HUBS_V1,
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

    private Producer<String, byte[]> initKafkaProducer() {
        Properties kafkaConfigs = new Properties();
        kafkaConfigs.put(ProducerConfig.CLIENT_ID_CONFIG, "collector-producer");
        kafkaConfigs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        kafkaConfigs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        kafkaConfigs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, BytesSerializer.class.getName());
        kafkaConfigs.put(ProducerConfig.LINGER_MS_CONFIG, 3000);
        return new KafkaProducer<>(kafkaConfigs);
    }

}
