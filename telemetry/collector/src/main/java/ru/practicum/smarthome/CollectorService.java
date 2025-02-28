package ru.practicum.smarthome;

import ru.practicum.smarthome.dto.hub.HubEvent;
import ru.practicum.smarthome.dto.sensor.SensorEvent;

public interface CollectorService {

    void collectSensorEvent(SensorEvent event);

    void collectHubEvent(HubEvent event);

}
