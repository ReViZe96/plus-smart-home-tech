package ru.yandex.practicum.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentDto {

    private String paymentId;
    private Double totalPayment;
    private Double deliveryTotal;
    private Double feeTotal;

}
