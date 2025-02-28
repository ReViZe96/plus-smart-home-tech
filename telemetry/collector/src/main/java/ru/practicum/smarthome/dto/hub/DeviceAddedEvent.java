package ru.practicum.smarthome.dto.hub;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.yandex.practicum.kafka.telemetry.event.DeviceTypeAvro;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString(callSuper = true)
public class DeviceAddedEvent extends DeviceEvent {

    @NotNull
    private DeviceTypeAvro deviceType;

    @Override
    public HubEventType getType() {
        return HubEventType.DEVICE_ADDED;
    }

}
