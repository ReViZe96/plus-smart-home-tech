package ru.practicum.smarthome;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.smarthome.dto.hub.HubEvent;
import ru.practicum.smarthome.dto.sensor.SensorEvent;

import javax.validation.Valid;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class CollectorController {

    private final CollectorService collectorService;


    @PostMapping("/sensors")
    public void collectSensorEvent(@Valid @RequestBody SensorEvent event) {
        collectorService.collectSensorEvent(event);
    }

    @PostMapping("/hubs")
    public void collectHubEvent(@Valid @RequestBody HubEvent event) {
        collectorService.collectHubEvent(event);
    }

}
