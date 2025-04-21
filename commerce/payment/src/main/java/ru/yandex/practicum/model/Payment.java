package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID paymentId;
    @Column(name = "total_cost")
    private Double totalPayment;
    @Column(name = "product_cost")
    private Double productTotal;
    @Column(name = "delivery_cost")
    private Double deliveryTotal;
    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
    @Column(name = "fee_cost")
    private Double feeTotal;

}
