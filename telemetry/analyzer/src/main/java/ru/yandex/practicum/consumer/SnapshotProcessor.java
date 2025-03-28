package ru.yandex.practicum.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.handler.SnapshotHandler;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.yandex.practicum.consumer.AnalyzerTopics.TELEMETRY_SNAPSHOTS_V1;

@Slf4j
@Component
public class SnapshotProcessor {

    private final KafkaConsumer<String, SensorsSnapshotAvro> consumer;
    private final SnapshotHandler snapshotHandler;
    private static final Map<TopicPartition, OffsetAndMetadata> currentOffsets = new HashMap<>();


    @Autowired
    public SnapshotProcessor(KafkaConsumer<String, SensorsSnapshotAvro> consumer, SnapshotHandler snapshotHandler) {
        this.consumer = consumer;
        this.snapshotHandler = snapshotHandler;
    }

    public void start() {
        try {
            consumer.subscribe(List.of(TELEMETRY_SNAPSHOTS_V1));
            Runtime.getRuntime().addShutdownHook(new Thread(consumer::wakeup));
            log.info("Анализатор подписался на топик " + TELEMETRY_SNAPSHOTS_V1);

            while (true) {
                ConsumerRecords<String, SensorsSnapshotAvro> records = consumer.poll(Duration.ofMillis(1000));
                log.info("Получены " + records.count() + " снимков состояния из топика " + TELEMETRY_SNAPSHOTS_V1);
                int count = 0;
                for (ConsumerRecord<String, SensorsSnapshotAvro> record : records) {
                    snapshotHandler.handle(record.value());
                    manageOffsets(record, count, consumer);
                    count++;

                }
                consumer.commitSync();
            }
        } catch (WakeupException ignored) {
            // игнорируем - закрываем консьюмер в блоке finally
        } catch (Exception e) {
            log.error("Произошла ошибка при . \n {} : \n {}", e.getMessage(),
                    e.getStackTrace());
        } finally {
            try {
                consumer.commitSync();
            } finally {
                consumer.close();
            }
        }
    }

    private static void manageOffsets(ConsumerRecord<String, SensorsSnapshotAvro> record, int count, KafkaConsumer<String, SensorsSnapshotAvro> consumer) {
        // обновляем текущий оффсет для топика-партиции
        currentOffsets.put(
                new TopicPartition(record.topic(), record.partition()),
                new OffsetAndMetadata(record.offset() + 1)
        );

        if (count % 10 == 0) {
            consumer.commitAsync(currentOffsets, (offsets, exception) -> {
                if (exception != null) {
                    log.warn("Ошибка во время фиксации оффсетов: {}", offsets, exception);
                }
            });
        }
    }

}