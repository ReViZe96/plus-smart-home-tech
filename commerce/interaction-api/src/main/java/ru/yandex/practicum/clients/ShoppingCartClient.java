package ru.yandex.practicum.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.clients.fallback.ShoppingCartClientFallback;
import ru.yandex.practicum.dto.request.ChangeProductQuantityRequest;
import ru.yandex.practicum.dto.ProductDto;
import ru.yandex.practicum.dto.ShoppingCartDto;

import java.util.Map;

@FeignClient(name = "shopping-cart", fallback = ShoppingCartClientFallback.class)
public interface ShoppingCartClient {

    /**
     * Получить актуальную корзину для авторизованного пользователя.
     *
     * @param username имя пользователя
     * @return Ранее созданная или новая корзина в онлайн магазине
     */
    @GetMapping("/api/v1/shopping-cart")
    ShoppingCartDto getUserCart(@RequestParam(required = true) String username);

    /**
     * Добавить товары в корзину.
     *
     * @param username имя пользователя
     * @param products мапа, где ключ - идентификатор товара, а значение - отобранное количество этого товара
     * @return Корзина товаров с изменениями
     */
    @PutMapping("/api/v1/shopping-cart")
    ShoppingCartDto addProductsToCart(@RequestParam(required = true) String username,
                                      @RequestBody(required = true) Map<String, Integer> products);

    /**
     * Деактивировать корзину товаров для пользователя.
     *
     * @param username имя пользователя
     */
    @DeleteMapping("/api/v1/shopping-cart")
    void deactivateUserCart(@RequestParam(required = true) String username);

    /**
     * Изменить состав товаров в корзине, т.е. удалить переданные в параметре products.
     *
     * @param username имя пользователя
     * @param products мапа, где ключ - идентификатор товара, а значение - удаляемое количество этого товара
     * @return Корзина товаров с изменениями
     */
    @PostMapping("/api/v1/shopping-cart/remove")
    ShoppingCartDto removeProductFromUserCart(@RequestParam(required = true) String username,
                                              @RequestBody(required = true) Map<String, Integer> products);

    /**
     * Изменить количество товаров в корзине.
     *
     * @param username              имя пользователя
     * @param changeProductQuantity отображение идентификатора товара на новое количество этого товара
     * @return Актуальный товар со всеми сведениями из БД
     */
    @PostMapping("/api/v1/shopping-cart/change-quantity")
    ProductDto setProductAmountInCart(@RequestParam(required = true) String username,
                                      @RequestBody(required = true) ChangeProductQuantityRequest changeProductQuantity);

}
