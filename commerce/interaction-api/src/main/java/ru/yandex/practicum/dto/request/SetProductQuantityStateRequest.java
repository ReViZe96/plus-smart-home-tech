package ru.yandex.practicum.dto.request;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class SetProductQuantityStateRequest {

    @NotNull
    private String productId;
    @NotNull
    private String quantityState;

}
