package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.ProductDto;
import ru.yandex.practicum.dto.ShoppingCartDto;
import ru.yandex.practicum.dto.request.ChangeProductQuantityRequest;

import java.util.Map;

public interface ShoppingCartService {

    ShoppingCartDto getCart(String username);

    ShoppingCartDto addProductsToCart(String username, Map<String, Integer> products);

    void clearCart(String username);

    ShoppingCartDto removeProductFromCart(String username, Map<String, Integer> products);

    ProductDto setProductAmountInCart(String username, ChangeProductQuantityRequest changeProductQuantity);

}
