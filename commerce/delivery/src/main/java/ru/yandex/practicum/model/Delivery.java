package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "deliveries")
@Getter
@Setter
@NoArgsConstructor
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;
    @Column(name = "from_warehouse_address", nullable = false)
    private String fromAddress;
    @Column(name = "to_warehouse_address", nullable = false)
    private String toAddress;
    @Column(name = "order_id", nullable = false)
    private String orderId;
    @Column(name = "state", nullable = false)
    @Enumerated(EnumType.STRING)
    private DeliveryState state;
    @Column(name = "volume")
    private Double volume;
    @Column(name = "weigh")
    private Double weigh;
    @Column(name = "is_fragile")
    private Boolean fragile;

}
