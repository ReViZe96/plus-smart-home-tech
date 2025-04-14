package ru.yandex.practicum.exception;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductNotFoundException extends RuntimeException {

    private String userMessage;

    public ProductNotFoundException(String userMessage) {
        this.userMessage = userMessage;
    }

}
