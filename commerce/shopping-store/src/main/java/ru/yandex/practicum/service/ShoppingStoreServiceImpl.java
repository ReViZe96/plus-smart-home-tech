package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dto.ProductDto;
import ru.yandex.practicum.dto.request.SetProductQuantityStateRequest;
import ru.yandex.practicum.exception.ProductNotFoundException;
import ru.yandex.practicum.mapper.ProductMapper;
import ru.yandex.practicum.model.Product;
import ru.yandex.practicum.model.ProductCategory;
import ru.yandex.practicum.repository.ProductRepository;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShoppingStoreServiceImpl implements ShoppingStoreService {

    private static final List<String> sortingTypes = Arrays.asList("asc", "desc", "ascending", "descending");

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public Page<ProductDto> getProductsByType(ProductCategory category, ru.yandex.practicum.other.Pageable pageable) {
        checkSortingType(pageable.getSort());
        Pageable page = PageRequest.of(pageable.getPage(), pageable.getSize(), Sort.by(pageable.getSort(), "name"));
        return productRepository.findAll(page).map(productMapper::productToProductDto);
    }

    @Override
    public ProductDto addProduct(ProductDto addingProduct) {
        Product addedProduct = productRepository.save(productMapper.productDtoToProduct(addingProduct));
        return productMapper.productToProductDto(addedProduct);
    }

    @Override
    public ProductDto updateProduct(ProductDto updatingProduct) {
        checkProductPresence(updatingProduct.getProductId());
        Product updatedProduct = productRepository.save(productMapper.productDtoToProduct(updatingProduct));
        return productMapper.productToProductDto(updatedProduct);
    }

    @Override
    public Boolean removeProduct(String productId) {
        checkProductPresence(productId);
        productRepository.deleteById(productId);
        return productRepository.findById(productId).isEmpty();
    }

    @Override
    public Boolean setQuantityState(SetProductQuantityStateRequest quantityState) {
        checkProductPresence(quantityState.getProductId());
        int updatedRow = productRepository.updateProductQuantityState(quantityState.getProductId(), quantityState.getQuantityState());
        return updatedRow != 0;
    }

    @Override
    public ProductDto getById(String productId) {
        checkProductPresence(productId);
        return productRepository.findById(productId).map(productMapper::productToProductDto).get();
    }


    private void checkSortingType(String sort) {
        for (String sorting : sortingTypes) {
            if (sorting.equalsIgnoreCase(sort)) {
                return;
            }
        }
        throw new IllegalArgumentException("Передан некорректный тип сортировки продуктов на странице: " + sort);
    }

    private void checkProductPresence(String productId) {
        if (productRepository.findById(productId).isEmpty()) {
            throw new ProductNotFoundException("Обновляемый продукт с id = " + productId + " не найден в системе");
        }
    }

}
