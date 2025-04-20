package ru.yandex.practicum.dto.request;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class ShippedToDeliveryRequest {

    @NotNull
    private String orderId;
    @NotNull
    private String deliveryId;

}
