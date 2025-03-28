package ru.yandex.practicum.configuration;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Properties;

public class KafkaInitialization {

    public static Consumer<String, SpecificRecordBase> initKafkaConsumer(String clientId, String groupId, String deserializer) {
        Properties kafkaConfigs = new Properties();
        kafkaConfigs.put(ConsumerConfig.CLIENT_ID_CONFIG, clientId);
        kafkaConfigs.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        kafkaConfigs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        kafkaConfigs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        kafkaConfigs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer);
        return new KafkaConsumer<>(kafkaConfigs);
    }

}
