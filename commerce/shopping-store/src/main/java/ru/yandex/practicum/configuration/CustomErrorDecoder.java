package ru.yandex.practicum.configuration;

import feign.Response;
import feign.codec.ErrorDecoder;
import ru.yandex.practicum.exception.NotAuthorizedUserExceptionNotAuthorizedUserException;
import ru.yandex.practicum.exception.ProductNotFoundException;

public class CustomErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() == 404) {
            return new ProductNotFoundException("Resource not found. For method: " + methodKey);
        }
        if (response.status() == 401) {
            return new NotAuthorizedUserExceptionNotAuthorizedUserException("User is not authorized. For method: " + methodKey);
        }
        return defaultDecoder.decode(methodKey, response);
    }

}
