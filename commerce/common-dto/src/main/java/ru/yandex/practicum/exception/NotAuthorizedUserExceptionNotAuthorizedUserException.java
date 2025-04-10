package ru.yandex.practicum.exception;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotAuthorizedUserExceptionNotAuthorizedUserException extends RuntimeException {

    //добавить поля в соответствии со спецификацией
    private String userMessage;

    public NotAuthorizedUserExceptionNotAuthorizedUserException(String userMessage) {
        this.userMessage = userMessage;
    }

}
