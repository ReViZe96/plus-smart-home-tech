package ru.yandex.practicum.service;

import org.springframework.data.domain.Page;
import ru.yandex.practicum.dto.ProductDto;
import ru.yandex.practicum.dto.request.SetProductQuantityStateRequest;
import ru.yandex.practicum.model.ProductCategory;
import ru.yandex.practicum.other.Pageable;

public interface ShoppingStoreService {

    Page<ProductDto> getProductsByType(ProductCategory category, Pageable pageable);

    ProductDto addProduct(ProductDto addingProduct);

    ProductDto updateProduct(ProductDto updatingProduct);

    Boolean removeProduct(String productId);

    Boolean setQuantityState(SetProductQuantityStateRequest quantityState);

    ProductDto getById(String productId);

}
