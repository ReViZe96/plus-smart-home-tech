package ru.yandex.practicum.exception;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SpecifiedProductAlreadyInWarehouseException extends RuntimeException {

    //добавить поля в соответствии со спецификацией
    private String userMessage;

    public SpecifiedProductAlreadyInWarehouseException(String userMessage) {
        this.userMessage = userMessage;
    }

}
