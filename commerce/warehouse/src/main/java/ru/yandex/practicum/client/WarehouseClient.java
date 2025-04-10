package ru.yandex.practicum.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.dto.request.AddProductToWarehouseRequest;
import ru.yandex.practicum.dto.AddressDto;
import ru.yandex.practicum.dto.BookedProductsDto;
import ru.yandex.practicum.dto.request.NewProductInWarehouseRequest;
import ru.yandex.practicum.dto.ShoppingCartDto;

@FeignClient(name = "warehouse")
public interface WarehouseClient {

    /**
     * Добавить новый товар на склад.
     *
     * @param newProductInWarehouse Описание нового товара для обработки складом.
     */
    @PutMapping("/api/v1/warehouse")
    //в случае, когда товар с данным описанием уже имеется на складе (400) - SpecifiedProductAlreadyInWarehouseException
    void addNewProductToWarehouse(@RequestBody(required = true) NewProductInWarehouseRequest newProductInWarehouse);

    /**
     * Предварительно проверить что количество товаров на складе достаточно для данной корзиный продуктов.
     *
     * @param shoppingCart корзина товаров
     * @return Общие сведения по бронированию
     */
    @PostMapping("/api/v1/warehouse/check")
    //в случае, когда товар из корзины не находится в требуемом количестве на складе (400) - ProductInShoppingCartLowQuantityInWarehouse
    BookedProductsDto checkProductAmountInWarehouse(@RequestBody(required = true) ShoppingCartDto shoppingCart);

    /**
     * Принять товар на склад.
     *
     * @param addProductToWarehouse запрос на добавление определенного количества определенного товара
     */
    @PostMapping("/api/v1/warehouse/add")
    //в случае, когда нет информации о товаре на складе (400) - NoSpecifiedProductInWarehouseException
    void reviseProductToWarehouse(@RequestBody(required = true) AddProductToWarehouseRequest addProductToWarehouse);

    /**
     * Предоставить адрес склада для расчёта доставки.
     *
     * @return Актуальный адрес склада
     */
    @GetMapping("/api/v1/warehouse/address")
    AddressDto getWarehouseAddress();

}
