package ru.yandex.practicum.dto.request;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class AddProductToWarehouseRequest {

    private String productId;
    @NotNull
    @Min(1)
    private Long quantity;

}
