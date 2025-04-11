package ru.yandex.practicum.dto;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.enums.ProductState;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class ProductDto {

    private String productId;
    @NotNull
    private String productName;
    @NotNull
    private String description;
    private String imageSrc;
    @NotNull
    private String quantityState;
    @NotNull
    private ProductState productState;
    private String productCategory;
    @NotNull
    @Min(1)
    private Double price;

}
