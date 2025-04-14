package ru.yandex.practicum.exception;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NoProductsInShoppingCartException extends RuntimeException {

    private String userMessage;

    public NoProductsInShoppingCartException(String userMessage) {
        this.userMessage = userMessage;
    }

}
