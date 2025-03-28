package ru.yandex.practicum.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.handler.event.hub.HubEventHandler;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static ru.yandex.practicum.consumer.AnalyzerTopics.TELEMETRY_HUBS_V1;

@Slf4j
@Component
public class HubEventProcessor implements Runnable {

    private final KafkaConsumer<String, HubEventAvro> consumer;
    private final Map<String, HubEventHandler> hubEventHandlers;
    private static final Map<TopicPartition, OffsetAndMetadata> currentOffsets = new HashMap<>();


    public HubEventProcessor(KafkaConsumer<String, HubEventAvro> consumer, Set<HubEventHandler> hubEventHandlers) {
        this.consumer = consumer;
        this.hubEventHandlers = hubEventHandlers.stream()
                .collect(Collectors.toMap(
                        HubEventHandler::getEventType,
                        Function.identity()
                ));
    }

    @Override
    public void run() {
        try {
            consumer.subscribe(List.of(TELEMETRY_HUBS_V1));
            Runtime.getRuntime().addShutdownHook(new Thread(consumer::wakeup));
            log.info("Анализатор подписался на топик " + TELEMETRY_HUBS_V1);

            while (true) {
                int count = 0;
                ConsumerRecords<String, HubEventAvro> records = consumer.poll(Duration.ofMillis(10000));
                log.info("Получены " + records.count() + " записей о событиях хаба из топика " + TELEMETRY_HUBS_V1);
                for (ConsumerRecord<String, HubEventAvro> record : records) {
                    handleRecord(record.value());
                    manageOffsets(record, count, consumer);
                    count++;
                }
                consumer.commitSync();
            }
        } catch (WakeupException ignored) {
            // игнорируем - закрываем консьюмер в блоке finally
        } catch (Exception e) {
            log.error("Произошла ошибка при обработке события хаба. \n {} : \n {}", e.getMessage(),
                    e.getStackTrace());
        } finally {
            try {
                consumer.commitSync();
            } finally {
                consumer.close();
            }
        }
    }

    private void handleRecord(HubEventAvro hubEventAvro) {
        String eventType = hubEventAvro.getPayload().getClass().getName();
        if (hubEventHandlers.containsKey(eventType)) {
            hubEventHandlers.get(eventType).handle(hubEventAvro);
        } else {
            throw new IllegalArgumentException("Обработчик для события: " + eventType + " не найден");
        }
    }

    private static void manageOffsets(ConsumerRecord<String, HubEventAvro> record, int count, KafkaConsumer<String, HubEventAvro> consumer) {
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
