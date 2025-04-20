package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "deliveries")
@Getter
@Setter
@NoArgsConstructor
public class Delivery {

    @Id
    @Column(name = "id")
    private String deliveryId;
    @Column(name = "from_warehouse_address", nullable = false)
    private String fromAddress;
    @Column(name = "to_warehouse_address", nullable = false)
    private String toAddress;
    @Column(name = "order_id", nullable = false)
    private String orderId;
    @Column(name = "state", nullable = false)
    @Enumerated(EnumType.STRING)
    private DeliveryState deliveryState;

}
