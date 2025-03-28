package ru.yandex.practicum.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.configuration.KafkaInitialization;
import ru.yandex.practicum.handler.event.hub.HubEventHandler;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static ru.yandex.practicum.serializer.AnalyzerTopics.TELEMETRY_HUBS_V1;

@Slf4j
@Component
public class HubEventProcessor implements Runnable {

    private final Consumer<String, SpecificRecordBase> consumer;
    private final Map<String, HubEventHandler> hubEventHandlers;


    public HubEventProcessor(Set<HubEventHandler> hubEventHandlers) {
        consumer = KafkaInitialization.initHubConsumer();
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
                ConsumerRecords<String, SpecificRecordBase> records = consumer.poll(Duration.ofMillis(5000));
                log.info("Получены " + records.count() + " записей о событиях хаба из топика " + TELEMETRY_HUBS_V1);
                for (ConsumerRecord<String, SpecificRecordBase> record : records) {
                    HubEventAvro hubEvent = (HubEventAvro) record.value();
                    handleRecord(hubEvent);
                }
                consumer.commitSync();
            }
        } catch (WakeupException ignored) {
            // игнорируем - закрываем консьюмер в блоке finally
        } catch (Exception e) {
            log.error("Произошла ошибка при обработке события хаба. \n {} : \n {}", e.getMessage(),
                    e.getStackTrace());
        } finally {
            consumer.commitSync();
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

}
