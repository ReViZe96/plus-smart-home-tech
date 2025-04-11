package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dto.ProductDto;
import ru.yandex.practicum.dto.ShoppingCartDto;
import ru.yandex.practicum.dto.request.ChangeProductQuantityRequest;
import ru.yandex.practicum.exception.NotAuthorizedUserException;
import ru.yandex.practicum.mapper.CartMapper;
import ru.yandex.practicum.model.Cart;
import ru.yandex.practicum.repository.CartRepository;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final CartRepository cartRepository;
    private final CartMapper cartMapper;


    @Override
    public ShoppingCartDto getCart(String username) {
        checkUsername(username);
        Optional<Cart> cart = cartRepository.findByUsername(username);
        if (cart.isPresent()) {
            return cartMapper.cartToShoppingCartDto(cart.get());
        }
        Cart newCart = new Cart();
        newCart.setUsername(username);
        return Optional.of(cartRepository.save(newCart)).map(cartMapper::cartToShoppingCartDto).get();
    }

    @Override
    public ShoppingCartDto addProductsToCart(String username, Map<String, Integer> products) {
        checkUsername(username);
        return ;
    }

    @Override
    public void clearCart(String username) {
        checkUsername(username);

    }

    //в случае, когда нет искомых товаров в корзине (400) - NoProductsInShoppingCartException
    @Override
    public ShoppingCartDto removeProductFromCart(String username, Map<String, Integer> products) {
        return ;
    }

    //в случае, когда нет искомых товаров в корзине (400) - NoProductsInShoppingCartException
    @Override
    public ProductDto setProductAmountInCart(String username, ChangeProductQuantityRequest changeProductQuantity) {
        checkUsername(username);
        return ;
    }


    private void checkUsername(String username) {
        if (username.isBlank()) {
            throw new NotAuthorizedUserException("Пользователя с именем: " + username + " не существует");
        }
    }

}
