package ru.yandex.practicum.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.exception.*;


@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(NotEnoughInfoInOrderToCalculateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequestException(final RuntimeException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(NoOrderFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUnauthorizedException(final RuntimeException e) {
        return new ErrorResponse(e.getMessage());
    }

}
