package ru.yandex.practicum.mapper;

import org.mapstruct.Mapper;
import ru.yandex.practicum.dto.ShoppingCartDto;
import ru.yandex.practicum.model.Cart;

@Mapper
public interface CartMapper {

    Cart shoppingCartDtoToCart(ShoppingCartDto shoppingCartDto);

    ShoppingCartDto cartToShoppingCartDto(Cart cart);

}
