package ru.yandex.practicum.configuration;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.util.Properties;

@Configuration
public class KafkaConfiguration {

    @Value("${analyzer.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${analyzer.kafka.snapshot-consumer.client-id}")
    private String snapshotClientId;

    @Value("${analyzer.kafka.snapshot-consumer.group-id}")
    private String snapshotGroupId;

    @Value("${analyzer.kafka.snapshot-consumer.value-deserializer}")
    private String snapshotValueDeserializer;

    @Value("${analyzer.kafka.hub-consumer.client-id}")
    private String hubClientId;

    @Value("${analyzer.kafka.hub-consumer.group-id}")
    private String hubGroupId;

    @Value("${analyzer.kafka.hub-consumer.value-deserializer}")
    private String hubValueDeserializer;

    @Value("${analyzer.kafka.key-deserializer}")
    private String keyDeserializer;

    @Bean
    public KafkaConsumer<String, SensorsSnapshotAvro> snapshotsConsumer() {
        Properties snapshotsCounsumerConfigs = new Properties();
        snapshotsCounsumerConfigs.put(ConsumerConfig.CLIENT_ID_CONFIG, snapshotClientId);
        snapshotsCounsumerConfigs.put(ConsumerConfig.GROUP_ID_CONFIG, snapshotGroupId);
        snapshotsCounsumerConfigs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        snapshotsCounsumerConfigs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, keyDeserializer);
        snapshotsCounsumerConfigs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, snapshotValueDeserializer);
        return new KafkaConsumer<>(snapshotsCounsumerConfigs);
    }

    @Bean
    public KafkaConsumer<String, HubEventAvro> hubConsumer() {
        Properties hubsConsumerConfigs = new Properties();
        hubsConsumerConfigs.put(ConsumerConfig.CLIENT_ID_CONFIG, hubClientId);
        hubsConsumerConfigs.put(ConsumerConfig.GROUP_ID_CONFIG, hubGroupId);
        hubsConsumerConfigs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        hubsConsumerConfigs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, keyDeserializer);
        hubsConsumerConfigs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, hubValueDeserializer);
        return new KafkaConsumer<>(hubsConsumerConfigs);
    }

}
