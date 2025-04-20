package ru.yandex.practicum.exception;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotEnoughInfoInOrderToCalculateException extends RuntimeException {

    private String userMessage;

    public NotEnoughInfoInOrderToCalculateException(String userMessage) {
        this.userMessage = userMessage;
    }

}
