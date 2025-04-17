package ru.yandex.practicum.exception;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotAuthorizedUserException extends RuntimeException {

    private String userMessage;

    public NotAuthorizedUserException(String userMessage) {
        this.userMessage = userMessage;
    }

}
