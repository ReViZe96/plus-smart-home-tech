package ru.yandex.practicum.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Warehouses")
@Getter
@Setter
@NoArgsConstructor
public class Warehouse {

    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "address_city")
    private String city;
    @Column(name = "address_street")
    private String street;
    @Column(name = "address_house")
    private String house;
    @Column(name = "address_country")
    private String country;
    @Column(name = "address_flat")
    private String flat;

}
