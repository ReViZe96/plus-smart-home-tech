package ru.practicum.smarthome.dto.sensor;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString(callSuper = true)
public class ClimatSensorEvent extends SensorEvent {

    @NotNull
    private int temperatureC;
    @NotNull
    private int humidity;
    @NotNull
    private int co2Level;

    @Override
    public SensorEventType getType() {
        return SensorEventType.CLIMATE_SENSOR_EVENT;
    }

}
