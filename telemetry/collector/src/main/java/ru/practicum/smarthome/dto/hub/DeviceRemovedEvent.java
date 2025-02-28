package ru.practicum.smarthome.dto.hub;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class DeviceRemovedEvent extends DeviceEvent {

    @Override
    public HubEventType getType() {
        return HubEventType.DEVICE_REMOVED;
    }

}
