package ru.yandex.practicum.clients.fallback;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.clients.ShoppingStoreClient;
import ru.yandex.practicum.dto.ProductDto;
import ru.yandex.practicum.dto.request.SetProductQuantityStateRequest;


@Component
@Slf4j
public class ShoppingStoreClientFallback implements ShoppingStoreClient {

    private static Logger logger = LoggerFactory.getLogger(ShoppingStoreClientFallback.class);

    private static final String SERVICE_UNAVAILABLE = "Сервис 'Витрина товаров' временно недоступен: ";
    static final ProductDto PRODUCT_STUB = ProductDto.builder()
            .productId("stubId")
            .productName("stubName")
            .build();

    @Override
    public ProductDto getProductsByType(String category, Pageable pageable) {
        logger.warn(SERVICE_UNAVAILABLE + "невозможно получить список товаров из категории {}.", category);
        return PRODUCT_STUB;
    }

    @Override
    public ProductDto createNewItem(ProductDto addingProduct) {
        logger.warn(SERVICE_UNAVAILABLE + "невозможно добавить новый товар {} в витрину.",
                addingProduct.getProductId());
        return PRODUCT_STUB;
    }

    @Override
    public ProductDto updateProductInfo(ProductDto updatingProduct) {
        logger.warn(SERVICE_UNAVAILABLE + "невозможно обновить данные о товаре {} в витрине.",
                updatingProduct.getProductId());
        return PRODUCT_STUB;
    }

    @Override
    public Boolean removeProduct(String productId) {
        logger.warn(SERVICE_UNAVAILABLE + "невозможно удалить товар {} с витрины.", productId);
        return false;
    }

    @Override
    public Boolean setProductQuantityState(SetProductQuantityStateRequest quantityState) {
        logger.warn(SERVICE_UNAVAILABLE + "невозможно изменить статус наличия товара {}.", quantityState.getProductId());
        return false;
    }

    @Override
    public ProductDto getProductById(String productId) {
        logger.warn(SERVICE_UNAVAILABLE + "невозможно получить информацию о товаре {}.", productId);
        return PRODUCT_STUB;
    }

}
