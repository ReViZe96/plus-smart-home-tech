package ru.yandex.practicum.configuration;

import feign.Response;
import feign.codec.ErrorDecoder;
import ru.yandex.practicum.exception.NotAuthorizedUserException;
import ru.yandex.practicum.exception.ProductNotFoundException;
import ru.yandex.practicum.exception.SpecifiedProductAlreadyInWarehouseException;

public class CustomErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() == 404) {
            return new ProductNotFoundException("Resource not found. For method: " + methodKey);
        }
        if (response.status() == 401) {
            return new NotAuthorizedUserException("User is not authorized. For method: " + methodKey);
        }
        return defaultDecoder.decode(methodKey, response);
    }

    //--------------------------shop-cart-exceptions----------------------------------
//            if (response.status() == 400) {
//        return new NoProductsInShoppingCartException("Products in cart not found. For method: " + methodKey);
//    }
//        if (response.status() == 401) {
//        return new NotAuthorizedUserException("User is not authorized. For method: " + methodKey);


    //--------------------------warehouse-exceptions----------------------------------
//            if (response.status() == 400) {
//        return new SpecifiedProductAlreadyInWarehouseException("Product already in warehouse. For method: " + methodKey);
//        //return new ProductInShoppingCartLowQuantityInWarehouse("Amount of product in warehouse is low. For method: " + methodKey);
//        //return new NoSpecifiedProductInWarehouseException("Product not exist in warehouse yet. For method: " + methodKey);
//    }
//        if (response.status() == 401) {
//        return new NotAuthorizedUserException("User is not authorized. For method: " + methodKey);
//    }


}
