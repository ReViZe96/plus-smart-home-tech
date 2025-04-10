package ru.yandex.practicum.configuration;

import feign.Response;
import feign.codec.ErrorDecoder;
import ru.yandex.practicum.exception.NoProductsInShoppingCartException;
import ru.yandex.practicum.exception.NotAuthorizedUserExceptionNotAuthorizedUserException;

public class CustomErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() == 400) {
            return new NoProductsInShoppingCartException("Products in cart not found. For method: " + methodKey);
        }
        if (response.status() == 401) {
            return new NotAuthorizedUserExceptionNotAuthorizedUserException("User is not authorized. For method: " + methodKey);
        }
        return defaultDecoder.decode(methodKey, response);
    }

}
