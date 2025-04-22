package ru.yandex.practicum.exception;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NoDeliveryFoundException extends RuntimeException {

    private String userMessage;

    public NoDeliveryFoundException(String userMessage) {
        this.userMessage = userMessage;
    }

}
