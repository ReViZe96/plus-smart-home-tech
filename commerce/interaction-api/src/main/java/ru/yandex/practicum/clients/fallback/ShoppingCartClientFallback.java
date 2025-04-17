package ru.yandex.practicum.clients.fallback;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.clients.ShoppingCartClient;
import ru.yandex.practicum.dto.ProductDto;
import ru.yandex.practicum.dto.ShoppingCartDto;
import ru.yandex.practicum.dto.request.ChangeProductQuantityRequest;

import java.util.HashMap;
import java.util.Map;

import static ru.yandex.practicum.clients.fallback.ShoppingStoreClientFallback.PRODUCT_STUB;

@Component
@Slf4j
public class ShoppingCartClientFallback implements ShoppingCartClient {

    private static Logger logger = LoggerFactory.getLogger(ShoppingCartClientFallback.class);

    private static final String SERVICE_UNAVAILABLE = "Сервис 'Корзина пользователя' временно недоступен: ";
    private static final ShoppingCartDto CART_STUB = ShoppingCartDto.builder()
            .shoppingCartId("0")
            .products(new HashMap<>())
            .build();

    @Override
    public ShoppingCartDto getUserCart(String username) {
        logger.warn(SERVICE_UNAVAILABLE + "невозможно получить актуальную корзину пользователя {}. ", username);
        return CART_STUB;
    }

    @Override
    public ShoppingCartDto addProductsToCart(String username, Map<String, Integer> products) {
        logger.warn(SERVICE_UNAVAILABLE + "невозможно добавить товары в корзину пользователя {}. ", username);
        return CART_STUB;
    }

    @Override
    public void deactivateUserCart(String username) {
        logger.warn(SERVICE_UNAVAILABLE + "невозможно деактивировать корзину товаров пользователя {}.", username);
    }

    @Override
    public ShoppingCartDto removeProductFromUserCart(String username, Map<String, Integer> products) {
        logger.warn(SERVICE_UNAVAILABLE + "невозможно удалить товары из корзины пользователя {}.", username);
        return CART_STUB;
    }

    @Override
    public ProductDto setProductAmountInCart(String username, ChangeProductQuantityRequest changeProductQuantity) {
        logger.warn(SERVICE_UNAVAILABLE + "невозможно изменить количество товаров в корзине пользователя {}.", username);
        return PRODUCT_STUB;
    }

}
