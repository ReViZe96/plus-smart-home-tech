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
    @Column(name = "address")
    private String address;

}
