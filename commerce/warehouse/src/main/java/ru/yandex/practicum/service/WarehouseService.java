package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.AddressDto;
import ru.yandex.practicum.dto.BookedProductsDto;
import ru.yandex.practicum.dto.DeliveryDto;
import ru.yandex.practicum.dto.ShoppingCartDto;
import ru.yandex.practicum.dto.request.AddProductToWarehouseRequest;
import ru.yandex.practicum.dto.request.AssemblyProductsForOrderRequest;
import ru.yandex.practicum.dto.request.NewProductInWarehouseRequest;
import ru.yandex.practicum.dto.request.ShippedToDeliveryRequest;

import java.util.Map;

public interface WarehouseService {

    void createNewItem(NewProductInWarehouseRequest newProductInWarehouse);

    DeliveryDto shippedProductsToDelivery(ShippedToDeliveryRequest shippedToDelivery);

    Boolean returnProductsToWarehouse(Map<String, Integer> returnedProducts);

    BookedProductsDto checkProductAmount(ShoppingCartDto shoppingCart);

    BookedProductsDto assemblyProductsForShipment(AssemblyProductsForOrderRequest assemblyProductsForShipment);

    void addProduct(AddProductToWarehouseRequest addProductToWarehouse);

    AddressDto getWarehouseById(String warehouseId);

    AddressDto addWarehouse(AddressDto newAddress);

}
