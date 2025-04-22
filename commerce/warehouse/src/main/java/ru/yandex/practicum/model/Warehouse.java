package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "warehouses")
@Getter
@Setter
@NoArgsConstructor
public class Warehouse {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "address_city", nullable = false)
    private String city;
    @Column(name = "address_street", nullable = false)
    private String street;
    @Column(name = "address_house", nullable = false)
    private String house;
    @Column(name = "address_country", nullable = false)
    private String country;
    @Column(name = "address_flat", nullable = false)
    private String flat;

}
