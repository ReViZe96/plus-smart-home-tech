package ru.yandex.practicum.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;
import ru.yandex.practicum.clients.fallback.ShoppingStoreClientFallback;
import ru.yandex.practicum.dto.ProductDto;
import ru.yandex.practicum.dto.request.SetProductQuantityStateRequest;

@FeignClient(name = "shopping-store", fallback = ShoppingStoreClientFallback.class)
public interface ShoppingStoreClient {

    /**
     * Получение списка товаров по типу в пагинированном виде.
     *
     * @param category тип товаров для возврата страницы: Управление, Датчики и т.д.
     */
    @GetMapping("/api/v1/shopping-store")
    ProductDto getProductsByType(@RequestParam(value = "category", required = true) String category,
                                 @RequestParam(value = "pageable", required = true) Pageable pageable);

    /**
     * Создание нового товара в ассортименте.
     *
     * @param addingProduct описательная часть вновь добавляемого товара в систему, например нового роутера и т.д.
     */
    @PutMapping("/api/v1/shopping-store")
    ProductDto createNewItem(@RequestBody(required = true) ProductDto addingProduct);

    /**
     * Обновление товара в ассортименте, например уточнение описания, характеристик и т.д.
     *
     * @param updatingProduct описательная часть изменяемого товара в системе
     */
    @PostMapping("/api/v1/shopping-store")
    ProductDto updateProductInfo(@RequestBody(required = true) ProductDto updatingProduct);

    /**
     * Удаление товара из ассортимента магазина. Функция для менеджерского состава.
     *
     * @param productId идентификатор товара в БД на удаление из ассортимента.
     * @return Признак успеха операции. true - если успешно, false - во всех остальных случаях.
     */
    @PostMapping("/api/v1/shopping-store/removeProductFromStore")
    Boolean removeProduct(@RequestBody(required = true) String productId);

    /**
     * Установка статуса по товару. API вызывается со стороны склада.
     *
     * @param quantityState запрос на изменение статуса товара в магазине, например: "Закончился", "Мало" и т.д.
     * @return Признак успеха операции. Статус успешно обновлен - true, false - во всех остальных случаях.
     */
    @PostMapping("/api/v1/shopping-store/quantityState")
    Boolean setProductQuantityState(@RequestBody SetProductQuantityStateRequest quantityState);

    /**
     * Получение сведений о товаре из БД по идентификатору.
     *
     * @param productId идентификатор товара в БД.
     * @return Актуальный товар со всеми сведениями из БД
     */
    @GetMapping("/api/v1/shopping-store/{productId}")
    ProductDto getProductById(@PathVariable(name = "productId", required = true) String productId);

}
