package ru.yandex.practicum.starter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;
import ru.yandex.practicum.service.AggregatorService;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static ru.yandex.practicum.serializer.AggregatorTopics.TELEMETRY_SENSORS_V1;
import static ru.yandex.practicum.serializer.AggregatorTopics.TELEMETRY_SNAPSHOTS_V1;

@Slf4j
@Component
@RequiredArgsConstructor
public class AggregatorStarter {

    private final AggregatorService aggregatorService;
    private final KafkaProducer<String, SpecificRecordBase> producer;
    private final KafkaConsumer<String, SpecificRecordBase> consumer;

    /**
     * Метод для начала процесса агрегации данных.
     * Подписывается на топики для получения событий от датчиков,
     * формирует снимок их состояния и записывает в кафку.
     */
    public void start() {
        try {
            consumer.subscribe(List.of(TELEMETRY_SENSORS_V1));
            Runtime.getRuntime().addShutdownHook(new Thread(consumer::wakeup));
            log.info("Агрегатор подписался на топик " + TELEMETRY_SENSORS_V1);

            while (true) {
                ConsumerRecords<String, SpecificRecordBase> records = consumer.poll(Duration.ofMillis(5000));
                log.info("Получены " + records.count() + " записей о событиях из топика " + TELEMETRY_SENSORS_V1);
                for (ConsumerRecord<String, SpecificRecordBase> record : records) {
                    SensorEventAvro sensorEvent = (SensorEventAvro) record.value();
                    Optional<SensorsSnapshotAvro> snapshot = aggregatorService.updateState(sensorEvent);
                    log.info("Снапшот по хабу {} актуализирован с учетом данных события {}", sensorEvent.getHubId(),
                            sensorEvent.getId());
                    snapshot.ifPresent(
                            sensorsSnapshotAvro -> sendToKafka(
                                    TELEMETRY_SNAPSHOTS_V1, sensorsSnapshotAvro.getHubId(), sensorsSnapshotAvro));
                }
                consumer.commitSync();
            }

        } catch (WakeupException ignored) {
            // игнорируем - закрываем консьюмер и продюсер в блоке finally
        } catch (Exception e) {
            log.error("Произошла ошибка при агрегации событий датчиков в снимки состояния. \n {} : \n {}", e.getMessage(),
                    e.getStackTrace());
        } finally {
            try {
                producer.flush();
                consumer.commitSync();
            } finally {
                consumer.close();
                producer.close();
            }
        }
    }

    public void sendToKafka(String topicName, String hubId, SpecificRecordBase snapshot) {
        ProducerRecord<String, SpecificRecordBase> producerSensorRecord = new ProducerRecord<>(
                topicName,
                null,
                System.currentTimeMillis(),
                hubId,
                snapshot);
        Future<RecordMetadata> message = producer.send(producerSensorRecord);
        try {
            message.get();
            log.info("Снапшот с актуализированными данными отправлен в топик {}", topicName);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Ошибка во время отправки сообщения в Kafka");
        }
    }

}
