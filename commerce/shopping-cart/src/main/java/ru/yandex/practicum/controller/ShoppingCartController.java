package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.ProductDto;
import ru.yandex.practicum.dto.ShoppingCartDto;
import ru.yandex.practicum.dto.request.ChangeProductQuantityRequest;
import ru.yandex.practicum.service.ShoppingCartService;

import java.util.Map;

@RestController("/api/v1/shopping-cart")
@RequiredArgsConstructor
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;

    /**
     * Получить актуальную корзину для авторизованного пользователя.
     * Если у пользователя еще нет корзины - создать ее.
     * @param username имя пользователя
     * @return Ранее созданная или новая корзина в онлайн магазине
     */
    @GetMapping
    public ResponseEntity<ShoppingCartDto> getUserCart(@RequestParam String username) {
        return ResponseEntity.ok(shoppingCartService.getCart(username));
    }

    /**
     * Добавить товары в корзину.
     *
     * @param username имя пользователя
     * @param products мапа, где ключ - идентификатор товара, а значение - отобранное количество этого товара
     * @return Корзина товаров с изменениями
     */
    @PutMapping
    public ResponseEntity<ShoppingCartDto> addProductsToCart(@RequestParam String username,
                                                             @RequestBody Map<String, Integer> products) {
        return ResponseEntity.ok(shoppingCartService.addProductsToCart(username, products));
    }

    /**
     * Деактивировать корзину товаров для пользователя.
     *
     * @param username имя пользователя
     */
    @DeleteMapping
    public ResponseEntity<Void> deactivateUserCart(@RequestParam String username) {
        shoppingCartService.deactivateCart(username);
        return ResponseEntity.ok().build();
    }

    /**
     * Изменить состав товаров в корзине, т.е. удалить переданные в параметре products.
     *
     * @param username имя пользователя
     * @param products мапа, где ключ - идентификатор товара, а значение - удаляемое количество этого товара
     * @return Корзина товаров с изменениями
     */
    @PostMapping("/remove")
    public ResponseEntity<ShoppingCartDto> removeProductFromUserCart(@RequestParam String username,
                                                     @RequestBody Map<String, Integer> products) {
        return ResponseEntity.ok(shoppingCartService.removeProductFromCart(username, products));
    }

    /**
     * Изменить количество товаров в корзине.
     *
     * @param username              имя пользователя
     * @param changeProductQuantity отображение идентификатора товара на новое количество этого товара
     * @return Актуальный товар со всеми сведениями из БД
     */
    @PostMapping("/change-quantity")
    public ResponseEntity<ProductDto> setProductAmountInCart(@RequestParam String username,
                                                             @RequestBody ChangeProductQuantityRequest changeProductQuantity) {
        return ResponseEntity.ok(shoppingCartService.setProductAmountInCart(username, changeProductQuantity));
    }

}

