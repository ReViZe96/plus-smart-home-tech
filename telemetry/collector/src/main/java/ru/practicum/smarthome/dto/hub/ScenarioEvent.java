package ru.practicum.smarthome.dto.hub;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString(callSuper = true)
public abstract class ScenarioEvent extends HubEvent {

    @NotNull
    @Size(min=3)
    private String name;

    public abstract HubEventType getType();

}
