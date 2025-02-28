package ru.practicum.smarthome.dto.hub;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class ScenarioRemovedEvent extends ScenarioEvent {

    @Override
    public HubEventType getType() {
        return HubEventType.SCENARIO_REMOVED;
    }

}
