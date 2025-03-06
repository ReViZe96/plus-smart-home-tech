package ru.practicum.smarthome.dto.hub;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString(callSuper = true)
public abstract class DeviceEvent extends HubEvent {

    @NotNull
    private String id;

    public abstract HubEventType getType();

}
