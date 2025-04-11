package ru.yandex.practicum.dto.request;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class ChangeProductQuantityRequest {

    @NotNull
    private String productId;
    @NotNull
    private Long newQuantity;

}
