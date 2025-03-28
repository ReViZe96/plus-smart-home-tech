package ru.yandex.practicum.configuration;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import ru.yandex.practicum.serializer.HubEventDeserializer;
import ru.yandex.practicum.serializer.SnapshotDeserializer;

import java.util.Properties;

public class KafkaInitialization {

    public static Consumer<String, SpecificRecordBase> initHubConsumer() {
        Properties kafkaConfigs = new Properties();
        kafkaConfigs.put(ConsumerConfig.CLIENT_ID_CONFIG, "hub-event-consumer");
        kafkaConfigs.put(ConsumerConfig.GROUP_ID_CONFIG, "hub-analyzer-group");
        kafkaConfigs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        kafkaConfigs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        kafkaConfigs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, HubEventDeserializer.class.getName());
        return new KafkaConsumer<>(kafkaConfigs);
    }

    public static Consumer<String, SpecificRecordBase> initSnapshotConsumer() {
        Properties kafkaConfigs = new Properties();
        kafkaConfigs.put(ConsumerConfig.CLIENT_ID_CONFIG, "snapshot-event-consumer");
        kafkaConfigs.put(ConsumerConfig.GROUP_ID_CONFIG, "snapshot-analyzer-group");
        kafkaConfigs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        kafkaConfigs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        kafkaConfigs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, SnapshotDeserializer.class.getName());
        return new KafkaConsumer<>(kafkaConfigs);
    }

}
