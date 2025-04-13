package ru.yandex.practicum.exception;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NoSpecifiedProductInWarehouseException extends RuntimeException {

    //добавить поля в соответствии со спецификацией
    private String userMessage;

    public NoSpecifiedProductInWarehouseException(String userMessage) {
        this.userMessage = userMessage;
    }

}
