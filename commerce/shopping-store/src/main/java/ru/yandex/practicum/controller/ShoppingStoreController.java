package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.ProductDto;
import ru.yandex.practicum.dto.request.SetProductQuantityStateRequest;
import ru.yandex.practicum.model.ProductCategory;
import ru.yandex.practicum.model.QuantityState;
import ru.yandex.practicum.service.ShoppingStoreService;

@RestController("/api/v1/shopping-store")
@RequiredArgsConstructor
public class ShoppingStoreController {

    private final ShoppingStoreService shoppingStoreService;


    /**
     * Получение списка товаров по типу в пагинированном виде.
     *
     * @param category тип товаров для возврата страницы: Управление, Датчики и т.д.
     */
    @GetMapping
    public ResponseEntity<Page<ProductDto>> getProductsByType(@RequestParam(value = "category") String category,
                                                              @RequestParam(value = "pageable") Pageable pageable) {
        ProductCategory productCategory = ProductCategory.from(category)
                .orElseThrow(() -> new IllegalArgumentException("Неизвестная категория: " + category));

        return ResponseEntity.ok(shoppingStoreService.getProductsByType(productCategory, pageable));
    }

    /**
     * Создание нового товара в ассортименте.
     *
     * @param addingProduct описательная часть вновь добавляемого товара в систему, например нового роутера и т.д.
     */
    @PutMapping
    public ResponseEntity<ProductDto> createNewItem(@RequestBody ProductDto addingProduct) {
        return ResponseEntity.ok(shoppingStoreService.addProduct(addingProduct));
    }

    /**
     * Обновление товара в ассортименте, например уточнение описания, характеристик и т.д.
     *
     * @param updatingProduct описательная часть изменяемого товара в системе
     */
    @PostMapping
    public ResponseEntity<ProductDto> updateProductInfo(@RequestBody ProductDto updatingProduct) {
        return ResponseEntity.ok(shoppingStoreService.updateProduct(updatingProduct));
    }

    /**
     * Удаление товара из ассортимента магазина. Функция для менеджерского состава.
     *
     * @param productId идентификатор товара в БД на удаление из ассортимента.
     * @return Признак успеха операции. true - если успешно, false - во всех остальных случаях.
     */
    @PostMapping("/removeProductFromStore")
    public ResponseEntity<Boolean> removeProduct(@RequestBody String productId) {
        return ResponseEntity.ok(shoppingStoreService.removeProduct(productId));
    }

    /**
     * Установка статуса по товару. API вызывается со стороны склада.
     *
     * @param quantityState запрос на изменение статуса товара в магазине, например: "Закончился", "Мало" и т.д.
     * @return Признак успеха операции. Статус успешно обновлен - true, false - во всех остальных случаях.
     */
    @PostMapping("/quantityState")
    public ResponseEntity<Boolean> setProductQuantityState(@RequestBody SetProductQuantityStateRequest quantityState) {
        QuantityState.from(quantityState.getQuantityState()).orElseThrow(
                () -> new IllegalArgumentException("Неизвестная количественная характеристика товара: "
                        + quantityState.getQuantityState()));

        return ResponseEntity.ok(shoppingStoreService.setQuantityState(quantityState));
    }

    /**
     * Получение сведений о товаре из БД по идентификатору.
     *
     * @param productId идентификатор товара в БД.
     * @return Актуальный товар со всеми сведениями из БД
     */
    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable(name = "productId") String productId) {
        return ResponseEntity.ok(shoppingStoreService.getById(productId));
    }

}

