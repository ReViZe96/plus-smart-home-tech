package ru.yandex.practicum.exception;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductInShoppingCartLowQuantityInWarehouse extends RuntimeException {

    private String userMessage;

    public ProductInShoppingCartLowQuantityInWarehouse(String userMessage) {
        this.userMessage = userMessage;
    }

}
