package ru.yandex.practicum.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class DeliveryDto {

    @NotNull
    private String deliveryId;
    @NotNull
    private AddressDto fromAddress;
    @NotNull
    private AddressDto toAddress;
    @NotNull
    private String orderId;
    @NotNull
    private String deliveryState;
    @NotNull
    private Double volume;
    @NotNull
    private Double weigh;
    @NotNull
    private Boolean fragile;

}
