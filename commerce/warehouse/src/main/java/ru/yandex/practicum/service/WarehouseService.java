package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.AddressDto;
import ru.yandex.practicum.dto.BookedProductsDto;
import ru.yandex.practicum.dto.ShoppingCartDto;
import ru.yandex.practicum.dto.request.AddProductToWarehouseRequest;
import ru.yandex.practicum.dto.request.NewProductInWarehouseRequest;

public interface WarehouseService {

    void createNewItem(NewProductInWarehouseRequest newProductInWarehouse);

    BookedProductsDto checkProductAmount(ShoppingCartDto shoppingCart);

    void addProduct(AddProductToWarehouseRequest addProductToWarehouse);

    AddressDto getWarehouseAddress();

}
