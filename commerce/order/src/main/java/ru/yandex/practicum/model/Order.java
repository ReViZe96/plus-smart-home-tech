package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID orderId;
    @Column(name = "cart_id")
    private String shoppingCartId;
    @ElementCollection
    @CollectionTable(name = "products", joinColumns = @JoinColumn(name = "order_id"))
    @MapKeyColumn(name = "product_id")
    @Column(name = "product_quantity", nullable = false)
    private Map<String, Integer> products = new HashMap<>();
    @Column(name = "payment_id")
    private String paymentId;
    @Column(name = "delivery_id")
    private String deliveryId;
    @Column(name = "order_state")
    @Enumerated(EnumType.STRING)
    private OrderState state;
    @Column(name = "delivery_weight")
    private Double deliveryWeight;
    @Column(name = "delivery_volume")
    private Double deliveryVolume;
    @Column(name = "delivery_fragile")
    private Boolean fragile;
    @Column(name = "total_price")
    private Double totalPrice;
    @Column(name = "delivery_price")
    private Double deliveryPrice;
    @Column(name = "product_price")
    private Double productPrice;

}
