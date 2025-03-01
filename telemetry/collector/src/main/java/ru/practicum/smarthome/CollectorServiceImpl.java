package ru.practicum.smarthome;

import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Service;
import ru.practicum.smarthome.dto.hub.*;
import ru.practicum.smarthome.dto.sensor.*;
import ru.practicum.smarthome.mapper.HubAvroMapper;
import ru.practicum.smarthome.mapper.SensorAvroMapper;
import ru.yandex.practicum.kafka.telemetry.event.*;

import javax.validation.ValidationException;
import java.util.Properties;


@Service
@RequiredArgsConstructor
public class CollectorServiceImpl implements CollectorService {

    private final SensorAvroMapper sensorMapper;
    private final HubAvroMapper hubMapper;

    private final Producer<String, SpecificRecordBase> producer = initKafkaProducer();


    @Override
    public void collectSensorEvent(SensorEvent event) {
        SensorEventAvro sensorEvent = SensorEventAvro.newBuilder()
                .setId(event.getId())
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setPayload(new Object())
                .build();

        switch (event.getType()) {
            case CLIMAT_SENSOR_EVENT:
                ClimatSensorEvent climatEvent = (ClimatSensorEvent) event;
                ClimateSensorAvro climatAvro = sensorMapper.climatSensorToAvro(climatEvent);
                sensorEvent.setPayload(climatAvro);
                break;
            case LIGHT_SENSOR_EVENT:
                LightSensorEvent lightEvent = (LightSensorEvent) event;
                LightSensorAvro lightAvro = sensorMapper.lightSensorToAvro(lightEvent);
                sensorEvent.setPayload(lightAvro);
                break;
            case MOTION_SENSOR_EVENT:
                MotionSensorEvent motionEvent = (MotionSensorEvent) event;
                MotionSensorAvro motionAvro = sensorMapper.motionSensorToAvro(motionEvent);
                sensorEvent.setPayload(motionAvro);
                break;
            case SWITCH_SENSOR_EVENT:
                SwitchSensorEvent switchEvent = (SwitchSensorEvent) event;
                SwitchSensorAvro switchAvro = sensorMapper.switchSensorToAvro(switchEvent);
                sensorEvent.setPayload(switchAvro);
                break;
            case TEMPERATURE_SENSOR_EVENT:
                TemperatureSensorEvent temperatureEvent = (TemperatureSensorEvent) event;
                TemperatureSensorAvro temperatureAvro = sensorMapper.temperatureSensorToAvro(temperatureEvent);
                sensorEvent.setPayload(temperatureAvro);
                break;
            default:
                throw new ValidationException("Указан неверный тип события, связанного с датчиком: " + event.getType());
        }

        ProducerRecord<String, SpecificRecordBase> producerSensorRecord = new ProducerRecord<>(
                TelemetryTopics.TELEMETRY_SENSORS_V1,
                null,
                System.currentTimeMillis(),
                event.getType().name(),
                sensorEvent);
        producer.send(producerSensorRecord);

    }

    @Override
    public void collectHubEvent(HubEvent event) {
        HubEventAvro hubEvent = HubEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setPayload(new Object())
                .build();

        switch (event.getType()) {
            case DEVICE_ADDED:
                DeviceAddedEvent deviceAddedEvent = (DeviceAddedEvent) event;
                DeviceAddedEventAvro deviceAddedAvro = hubMapper.deviceAddedToAvro(deviceAddedEvent);
                hubEvent.setPayload(deviceAddedAvro);
                break;
            case DEVICE_REMOVED:
                DeviceRemovedEvent deviceRemovedEvent = (DeviceRemovedEvent) event;
                DeviceRemovedEventAvro deviceRemovedAvro = hubMapper.deviceRemovedToAvro(deviceRemovedEvent);
                hubEvent.setPayload(deviceRemovedAvro);
                break;
            case SCENARIO_ADDED:
                ScenarioAddedEvent scenarioAddedEvent = (ScenarioAddedEvent) event;
                ScenarioAddedEventAvro scenarioAddedAvro = hubMapper.scenarioAddedToAvro(scenarioAddedEvent);
                hubEvent.setPayload(scenarioAddedAvro);
                break;
            case SCENARIO_REMOVED:
                ScenarioRemovedEvent scenarioRemovedEvent = (ScenarioRemovedEvent) event;
                ScenarioRemovedEventAvro scenarioRemovedEventAvro = hubMapper.scenarioRemovedToAvro(scenarioRemovedEvent);
                hubEvent.setPayload(scenarioRemovedEventAvro);
                break;
            default:
                throw new ValidationException("Указан неверный тип события, связанного с хабом: " + event.getType());
        }

        ProducerRecord<String, SpecificRecordBase> producerHubRecord = new ProducerRecord<>(
                TelemetryTopics.TELEMETRY_HUBS_V1,
                null,
                System.currentTimeMillis(),
                event.getType().name(),
                hubEvent);
        producer.send(producerHubRecord);

    }


    private Producer<String, SpecificRecordBase> initKafkaProducer() {
        Properties kafkaConfigs = new Properties();
        kafkaConfigs.put(ProducerConfig.CLIENT_ID_CONFIG, "collector-producer");
        kafkaConfigs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        kafkaConfigs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        kafkaConfigs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "ru.practicum.smarthome.TelemetrySerializer");
        kafkaConfigs.put(ProducerConfig.LINGER_MS_CONFIG, 10000);
        return new KafkaProducer<>(kafkaConfigs);

    }

}
