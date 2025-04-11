package ru.yandex.practicum.dto.request;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.dto.DimensionDto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class NewProductInWarehouseRequest {

    @NotNull
    private String productId;
    private Boolean fragile;
    @NotNull
    private DimensionDto dimension;
    @Min(1)
    private Double weight;

}
