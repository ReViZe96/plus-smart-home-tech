package ru.yandex.practicum.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "items")
@Getter
@Setter
@NoArgsConstructor
public class WarehouseItem {

    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "quantity_in_warehouse")
    private Integer quantity;
    @Column(name = "fragile")
    private Boolean fragile;
    @Column(name = "weight")
    private Double weight;
    @Column(name = "width")
    private Double width;
    @Column(name = "height")
    private Double height;
    @Column(name = "depth")
    private Double depth;

}
