package ru.yandex.practicum.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Map;

@Data
@Builder
public class OrderDto {

    @NotNull
    private String orderId;
    private String shoppingCartId;
    @NotNull
    private Map<String, Integer> products;
    private String paymentId;
    private String deliveryId;
    private String state;
    private Double deliveryWeight;
    private Double deliveryVolume;
    private Boolean fragile;
    private Double totalPrice;
    private Double deliveryPrice;
    private Double productPrice;

}
