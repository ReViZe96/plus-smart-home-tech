package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.AddressDto;
import ru.yandex.practicum.dto.BookedProductsDto;
import ru.yandex.practicum.dto.ShoppingCartDto;
import ru.yandex.practicum.dto.request.AddProductToWarehouseRequest;
import ru.yandex.practicum.dto.request.NewProductInWarehouseRequest;
import ru.yandex.practicum.service.WarehouseService;

@RestController("/api/v1/warehouse")
@RequiredArgsConstructor
public class WarehouseController {

    private final WarehouseService warehouseService;

    /**
     * Добавить новую позицию на склад.
     *
     * @param newProductInWarehouse Описание новой позиции товара, обрабатываемой складом.
     */
    @PutMapping
    public ResponseEntity<Void> createNewItemInWarehouse(@RequestBody NewProductInWarehouseRequest newProductInWarehouse) {
        warehouseService.createNewItem(newProductInWarehouse);
        return ResponseEntity.ok().build();
    }

    /**
     * Предварительно проверить что количество товаров на складе достаточно для данной корзиный продуктов.
     *
     * @param shoppingCart корзина товаров
     * @return Общие сведения по бронированию
     */
    @PostMapping("/check")
    public ResponseEntity<BookedProductsDto> checkProductAmountInWarehouse(@RequestBody ShoppingCartDto shoppingCart) {
        return ResponseEntity.ok(warehouseService.checkProductAmount(shoppingCart));
    }

    /**
     * Принять товар на склад.
     *
     * @param addProductToWarehouse запрос на добавление определенного количества определенного товара
     */
    @PostMapping("/add")
    public ResponseEntity<Void> addProductToWarehouse(@RequestBody AddProductToWarehouseRequest addProductToWarehouse) {
        warehouseService.addProduct(addProductToWarehouse);
        return ResponseEntity.ok().build();
    }

    /**
     * Предоставить адрес склада для расчёта доставки.
     *
     * @return Актуальный адрес склада
     */
    @GetMapping("/address")
    public ResponseEntity<AddressDto> getWarehouseAddress() {
        return ResponseEntity.ok(warehouseService.getWarehouseAddress());
    }

}
