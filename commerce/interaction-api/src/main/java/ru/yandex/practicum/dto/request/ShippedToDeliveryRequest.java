package ru.yandex.practicum.dto.request;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@Builder
public class ShippedToDeliveryRequest {

    @NotNull
    private String orderId;
    @NotNull
    private UUID deliveryId;

}
