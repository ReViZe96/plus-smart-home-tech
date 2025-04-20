package ru.yandex.practicum.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.clients.fallback.WarehouseClientFallback;
import ru.yandex.practicum.dto.request.AddProductToWarehouseRequest;
import ru.yandex.practicum.dto.AddressDto;
import ru.yandex.practicum.dto.BookedProductsDto;
import ru.yandex.practicum.dto.request.AssemblyProductsForOrderRequest;
import ru.yandex.practicum.dto.request.NewProductInWarehouseRequest;
import ru.yandex.practicum.dto.ShoppingCartDto;
import ru.yandex.practicum.dto.request.ShippedToDeliveryRequest;

import java.util.Map;

@FeignClient(name = "warehouse", fallback = WarehouseClientFallback.class)
public interface WarehouseClient {

    /**
     * Добавить новую позицию на склад.
     *
     * @param newProductInWarehouse Описание новой позиции товара, обрабатываемой складом.
     */
    @PutMapping("/api/v1/warehouse")
    void createNewItemInWarehouse(@RequestBody(required = true) NewProductInWarehouseRequest newProductInWarehouse);

    /**
     * Передать товары в доставку.
     * Метод должен обновить информацию о собранном заказе в базе данных склада:
     * добавить в него идентификатор доставки, который вернул сервис доставки,
     * присвоить идентификатор доставки во внутреннем хранилище собранных товаров заказа.
     * Вызывается из сервиса доставки.
     *
     * @param shippedToDelivery запрос на передачу товаров в доставку
     */
    @PostMapping("/api/v1/warehouse/shipped")
    Boolean shippedProductsToDelivery(@RequestBody(required = true) ShippedToDeliveryRequest shippedToDelivery);

    /**
     * Принять возврат товаров на склад.
     * Если товар возвращается на склад, нужно увеличить остаток.
     * Метод принимает список товаров с количеством и увеличивает доступный остаток по данным товарам.
     *
     * @param returnedProducts мапа, где ключ - идентификатор товара, а значение - возвращаемое количество этого товара
     */
    @PostMapping("/api/v1/warehouse/return")
    Boolean returnProductsToWarehouse(@RequestBody(required = true) Map<String, Integer> returnedProducts);

    /**
     * Предварительно проверить что количество товаров на складе достаточно для данной корзиный продуктов.
     *
     * @param shoppingCart корзина товаров
     * @return Общие сведения по бронированию
     */
    @PostMapping("/api/v1/warehouse/check")
    BookedProductsDto checkProductAmountInWarehouse(@RequestBody(required = true) ShoppingCartDto shoppingCart);

    /**
     * Собрать товары к заказу для подготовки к отправке.
     * Метод получает список товаров и идентификатор заказа.
     * По списку повторно проверяется наличие заказанных товаров в нужном количестве
     * и уменьшается их доступный остаток.
     *
     * @param assemblyProductsForShipment запрос на сбор заказа из товаров (список товаров и идентификатор заказа)
     * @return Общие сведения по бронированию
     */
    @PostMapping("/api/v1/warehouse/assembly")
    BookedProductsDto assemblyProductsForShipment(@RequestBody(required = true) AssemblyProductsForOrderRequest assemblyProductsForShipment);

    /**
     * Принять товар на склад.
     *
     * @param addProductToWarehouse запрос на добавление определенного количества определенного товара
     */
    @PostMapping("/api/v1/warehouse/add")
    void reviseProductToWarehouse(@RequestBody(required = true) AddProductToWarehouseRequest addProductToWarehouse);

    /**
     * Предоставить адрес склада для расчёта доставки.
     *
     * @return Актуальный адрес склада
     */
    @GetMapping("/api/v1/warehouse/address")
    AddressDto getWarehouseAddress();

}
