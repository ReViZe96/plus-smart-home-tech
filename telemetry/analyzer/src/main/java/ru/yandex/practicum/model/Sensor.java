package ru.yandex.practicum.model;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

@Entity
@Table(name = "sensors")
@Data
@Builder
public class Sensor {

    @Id
    private String id;
    @Column(name = "hub_id")
    private String hubId;

}
