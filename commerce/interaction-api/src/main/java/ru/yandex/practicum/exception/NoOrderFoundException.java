package ru.yandex.practicum.exception;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NoOrderFoundException extends RuntimeException {

    private String userMessage;

    public NoOrderFoundException(String userMessage) {
        this.userMessage = userMessage;
    }

}
