package ru.yandex.practicum.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.configuration.KafkaInitialization;
import ru.yandex.practicum.handler.SnapshotHandler;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;
import ru.yandex.practicum.serializer.SnapshotDeserializer;

import java.time.Duration;
import java.util.List;

import static ru.yandex.practicum.serializer.AnalyzerTopics.TELEMETRY_SNAPSHOTS_V1;

@Slf4j
@Component
public class SnapshotProcessor {

    @Value("${analyzer.snapshot-consumer.client-id}")
    private String clientId;

    @Value("${analyzer.snapshot-consumer.group-id}")
    private String groupId;

    private final Consumer<String, SpecificRecordBase> consumer;
    private final SnapshotHandler snapshotHandler;

    @Autowired
    public SnapshotProcessor(SnapshotHandler snapshotHandler) {
        consumer = KafkaInitialization.initKafkaConsumer(clientId, groupId, SnapshotDeserializer.class.getName());
        this.snapshotHandler = snapshotHandler;
    }

    public void start() {
        try {
            consumer.subscribe(List.of(TELEMETRY_SNAPSHOTS_V1));
            Runtime.getRuntime().addShutdownHook(new Thread(consumer::wakeup));
            log.info("Анализатор подписался на топик " + TELEMETRY_SNAPSHOTS_V1);

            while (true) {
                ConsumerRecords<String, SpecificRecordBase> records = consumer.poll(Duration.ofMillis(5000));
                log.info("Получены " + records.count() + " снимков состояния из топика " + TELEMETRY_SNAPSHOTS_V1);
                for (ConsumerRecord<String, SpecificRecordBase> record : records) {
                    SensorsSnapshotAvro snapshot = (SensorsSnapshotAvro) record.value();
                    snapshotHandler.handle(snapshot);

                }
                consumer.commitSync();
            }
        } catch (WakeupException ignored) {
            // игнорируем - закрываем консьюмер в блоке finally
        } catch (Exception e) {
            log.error("Произошла ошибка при . \n {} : \n {}", e.getMessage(),
                    e.getStackTrace());
        } finally {
            consumer.commitSync();
        }
    }

}