package ru.yandex.practicum.clients.fallback;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.clients.WarehouseClient;
import ru.yandex.practicum.dto.AddressDto;
import ru.yandex.practicum.dto.BookedProductsDto;
import ru.yandex.practicum.dto.DeliveryDto;
import ru.yandex.practicum.dto.ShoppingCartDto;
import ru.yandex.practicum.dto.request.AddProductToWarehouseRequest;
import ru.yandex.practicum.dto.request.AssemblyProductsForOrderRequest;
import ru.yandex.practicum.dto.request.NewProductInWarehouseRequest;
import ru.yandex.practicum.dto.request.ShippedToDeliveryRequest;

import java.util.Map;

import static ru.yandex.practicum.clients.fallback.DeliveryClientFallback.DELIVERY_STUB;

@Component
@Slf4j
public class WarehouseClientFallback implements WarehouseClient {

    private static final String SERVICE_UNAVAILABLE = "Сервис 'Склад' временно недоступен: ";
    private static final BookedProductsDto BOOKING_STUB = BookedProductsDto.builder().build();
    private static final AddressDto ADDRESS_STUB = AddressDto.builder().build();

    @Override
    public void createNewItemInWarehouse(NewProductInWarehouseRequest newProductInWarehouse) {
        log.warn(SERVICE_UNAVAILABLE + "невозможно добавить новую позицию {} на склад.",
                newProductInWarehouse.getProductId());
    }

    @Override
    public DeliveryDto shippedProductsToDelivery(ShippedToDeliveryRequest shippedToDelivery) {
        log.warn(SERVICE_UNAVAILABLE + "невозможно передать товары в доставку {} со склада.",
                shippedToDelivery.getDeliveryId());
        return DELIVERY_STUB;
    }

    @Override
    public Boolean returnProductsToWarehouse(Map<String, Integer> returnedProducts) {
        log.warn(SERVICE_UNAVAILABLE + "невозможно принять возврат товаров на склад.");
        return false;
    }


    @Override
    public BookedProductsDto checkProductAmountInWarehouse(ShoppingCartDto shoppingCart) {
        log.warn(SERVICE_UNAVAILABLE + "невозможно проверить достаточно ли товара на складе для заказа из корзины {}.",
                shoppingCart.getShoppingCartId());
        return BOOKING_STUB;
    }

    @Override
    public BookedProductsDto assemblyProductsForShipment(AssemblyProductsForOrderRequest assemblyProductsForShipment) {
        log.warn(SERVICE_UNAVAILABLE + "невозможно собрать товары к заказу {} для подготовки к отправке.",
                assemblyProductsForShipment.getOrderId());
        return BOOKING_STUB;
    }


    @Override
    public void reviseProductToWarehouse(AddProductToWarehouseRequest addProductToWarehouse) {
        log.warn(SERVICE_UNAVAILABLE + "невозможно принять товар {} на склад.", addProductToWarehouse.getProductId());
    }

    @Override
    public AddressDto getWarehouseAddress(String warehouseId) {
        log.warn(SERVICE_UNAVAILABLE + "невозможно предоставить адрес склада по его идентификатору {}.", warehouseId);
        return ADDRESS_STUB;
    }

    @Override
    public AddressDto addWarehouseAddress(AddressDto addressDto) {
        log.warn(SERVICE_UNAVAILABLE + "невозможно добавить склад по новому адресу.");
        return ADDRESS_STUB;
    }

}
