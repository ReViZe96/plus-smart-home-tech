package ru.yandex.practicum.exception;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductInShoppingCartLowQuantityInWarehouse extends RuntimeException {

    //добавить поля в соответствии со спецификацией
    private String userMessage;

    public ProductInShoppingCartLowQuantityInWarehouse(String userMessage) {
        this.userMessage = userMessage;
    }

}
