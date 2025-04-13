package ru.yandex.practicum.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Map;

@Data
@Builder
public class ShoppingCartDto {

    @NotNull
    private String shoppingCartId;
    @NotNull
    private Map<String, Integer> products;

}
