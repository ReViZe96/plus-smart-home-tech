package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.AddressDto;
import ru.yandex.practicum.dto.BookedProductsDto;
import ru.yandex.practicum.dto.DeliveryDto;
import ru.yandex.practicum.dto.ShoppingCartDto;
import ru.yandex.practicum.dto.request.AddProductToWarehouseRequest;
import ru.yandex.practicum.dto.request.AssemblyProductsForOrderRequest;
import ru.yandex.practicum.dto.request.NewProductInWarehouseRequest;
import ru.yandex.practicum.dto.request.ShippedToDeliveryRequest;
import ru.yandex.practicum.service.WarehouseService;

import java.util.Map;

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
     * Передать товары в доставку.
     * Метод должен обновить информацию о собранном заказе в базе данных склада:
     * добавить в него идентификатор доставки, который вернул сервис доставки,
     * присвоить идентификатор доставки во внутреннем хранилище собранных товаров заказа.
     * Вызывается из сервиса доставки.
     *
     * @param shippedToDelivery запрос на передачу товаров в доставку
     */
    @PostMapping("/shipped")
    ResponseEntity<DeliveryDto> shippedProductsToDelivery(@RequestBody(required = true) ShippedToDeliveryRequest shippedToDelivery) {
        return ResponseEntity.ok(warehouseService.shippedProductsToDelivery(shippedToDelivery));
    }

    /**
     * Принять возврат товаров на склад.
     * Если товар возвращается на склад, нужно увеличить остаток.
     * Метод принимает список товаров с количеством и увеличивает доступный остаток по данным товарам.
     *
     * @param returnedProducts мапа, где ключ - идентификатор товара, а значение - возвращаемое количество этого товара
     */
    @PostMapping("/return")
    ResponseEntity<Boolean> returnProductsToWarehouse(@RequestBody(required = true) Map<String, Integer> returnedProducts) {
        return ResponseEntity.ok(warehouseService.returnProductsToWarehouse(returnedProducts));
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
     * Собрать товары к заказу для подготовки к отправке.
     * Метод получает список товаров и идентификатор заказа.
     * По списку повторно проверяется наличие заказанных товаров в нужном количестве
     * и уменьшается их доступный остаток.
     *
     * @param assemblyProductsForShipment запрос на сбор заказа из товаров (список товаров и идентификатор заказа)
     * @return Общие сведения по бронированию
     */
    @PostMapping("/assembly")
    ResponseEntity<BookedProductsDto> assemblyProductsForShipment(@RequestBody(required = true) AssemblyProductsForOrderRequest assemblyProductsForShipment) {
        return ResponseEntity.ok(warehouseService.assemblyProductsForShipment(assemblyProductsForShipment));
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

    /**
     * Добавить новый склад в систему.
     *
     * @param addressDto адрес нового склада
     * @return новый адрес склада
     */
    @PostMapping("/address")
    public ResponseEntity<AddressDto> addWarehouseAddress(AddressDto addressDto) {
        return ResponseEntity.ok(warehouseService.addWarehouse(addressDto));
    }

}
