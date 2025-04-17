package ru.yandex.practicum.clients.fallback;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.clients.WarehouseClient;
import ru.yandex.practicum.dto.AddressDto;
import ru.yandex.practicum.dto.BookedProductsDto;
import ru.yandex.practicum.dto.ShoppingCartDto;
import ru.yandex.practicum.dto.request.AddProductToWarehouseRequest;
import ru.yandex.practicum.dto.request.NewProductInWarehouseRequest;

@Component
@Slf4j
public class WarehouseClientFallback implements WarehouseClient {

    private static Logger logger = LoggerFactory.getLogger(WarehouseClientFallback.class);

    private static final String SERVICE_UNAVAILABLE = "Сервис 'Склад' временно недоступен: ";
    private static final BookedProductsDto BOOKING_STUB = BookedProductsDto.builder().build();
    private static final AddressDto ADDRESS_STUB = AddressDto.builder().build();

    @Override
    public void createNewItemInWarehouse(NewProductInWarehouseRequest newProductInWarehouse) {
        logger.warn(SERVICE_UNAVAILABLE + "невозможно добавить новую позицию {} на склад.",
                newProductInWarehouse.getProductId());
    }

    @Override
    public BookedProductsDto checkProductAmountInWarehouse(ShoppingCartDto shoppingCart) {
        logger.warn(SERVICE_UNAVAILABLE + "невозможно проверить достаточно ли товара на складе для заказа из корзины {}.",
                shoppingCart.getShoppingCartId());
        return BOOKING_STUB;
    }

    @Override
    public void reviseProductToWarehouse(AddProductToWarehouseRequest addProductToWarehouse) {
        logger.warn(SERVICE_UNAVAILABLE + "невозможно принять товар {} на склад.", addProductToWarehouse.getProductId());
    }

    @Override
    public AddressDto getWarehouseAddress() {
        logger.warn(SERVICE_UNAVAILABLE + "невозможно предоставить адрес склада.");
        return ADDRESS_STUB;
    }

}
