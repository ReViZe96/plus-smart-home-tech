package ru.yandex.practicum.dto.request;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.dto.AddressDto;
import ru.yandex.practicum.dto.ShoppingCartDto;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class CreateNewOrderRequest {

    @NotNull
    private ShoppingCartDto shoppingCart;
    @NotNull
    private AddressDto deliveryAddress;

}
