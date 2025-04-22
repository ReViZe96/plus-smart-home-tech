package ru.yandex.practicum.clients.fallback;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.clients.ShoppingStoreClient;
import ru.yandex.practicum.dto.ProductDto;
import ru.yandex.practicum.dto.request.SetProductQuantityStateRequest;


@Component
@Slf4j
public class ShoppingStoreClientFallback implements ShoppingStoreClient {

    private static final String SERVICE_UNAVAILABLE = "Сервис 'Витрина товаров' временно недоступен: ";
    static final ProductDto PRODUCT_STUB = ProductDto.builder()
            .productId("stubId")
            .productName("stubName")
            .build();

    @Override
    public ProductDto getProductsByType(String category, Pageable pageable) {
        log.warn(SERVICE_UNAVAILABLE + "невозможно получить список товаров из категории {}.", category);
        return PRODUCT_STUB;
    }

    @Override
    public ProductDto createNewItem(ProductDto addingProduct) {
        log.warn(SERVICE_UNAVAILABLE + "невозможно добавить новый товар {} в витрину.",
                addingProduct.getProductId());
        return PRODUCT_STUB;
    }

    @Override
    public ProductDto updateProductInfo(ProductDto updatingProduct) {
        log.warn(SERVICE_UNAVAILABLE + "невозможно обновить данные о товаре {} в витрине.",
                updatingProduct.getProductId());
        return PRODUCT_STUB;
    }

    @Override
    public Boolean removeProduct(String productId) {
        log.warn(SERVICE_UNAVAILABLE + "невозможно удалить товар {} с витрины.", productId);
        return false;
    }

    @Override
    public Boolean setProductQuantityState(SetProductQuantityStateRequest quantityState) {
        log.warn(SERVICE_UNAVAILABLE + "невозможно изменить статус наличия товара {}.", quantityState.getProductId());
        return false;
    }

    @Override
    public ProductDto getProductById(String productId) {
        log.warn(SERVICE_UNAVAILABLE + "невозможно получить информацию о товаре {}.", productId);
        return PRODUCT_STUB;
    }

}
