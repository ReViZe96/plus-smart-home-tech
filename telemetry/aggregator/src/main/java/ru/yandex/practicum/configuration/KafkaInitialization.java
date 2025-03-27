package ru.yandex.practicum.configuration;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import ru.yandex.practicum.serializer.AggregatorSerializer;
import ru.yandex.practicum.serializer.SensorEventDeserializer;

import java.util.Properties;

public class KafkaInitialization {

    public static Consumer<String, SpecificRecordBase> initKafkaConsumer() {
        Properties kafkaConfigs = new Properties();
        kafkaConfigs.put(ConsumerConfig.CLIENT_ID_CONFIG, "sensor-event-consumer");
        kafkaConfigs.put(ConsumerConfig.GROUP_ID_CONFIG, "aggregator-group");
        kafkaConfigs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        kafkaConfigs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        kafkaConfigs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, SensorEventDeserializer.class.getName());
        return new KafkaConsumer<>(kafkaConfigs);
    }

    public static Producer<String, SpecificRecordBase> initKafkaProducer() {
        Properties kafkaConfigs = new Properties();
        kafkaConfigs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        kafkaConfigs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        kafkaConfigs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, AggregatorSerializer.class.getName());
        kafkaConfigs.put(ProducerConfig.LINGER_MS_CONFIG, 3000);
        return new KafkaProducer<>(kafkaConfigs);
    }

}
