package ru.yandex.practicum.configuration;

import feign.Response;
import feign.codec.ErrorDecoder;
import ru.yandex.practicum.exception.NotAuthorizedUserException;
import ru.yandex.practicum.exception.ProductInShoppingCartLowQuantityInWarehouse;
import ru.yandex.practicum.exception.ProductNotFoundException;

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
        if (response.status() == 400) {
            return new ProductInShoppingCartLowQuantityInWarehouse("Amount of product in warehouse is low. " +
                    "For method: " + methodKey);
        }
        return defaultDecoder.decode(methodKey, response);
    }

//    --------------------------Other Custom Exceptions:----------------------------------------------------------------
//    ----- shopping-cart:
//    - 400 ---> NoProductsInShoppingCartException("Products in cart not found. For method: " + methodKey);
//    ----- warehouse:
//    - 400 ---> SpecifiedProductAlreadyInWarehouseException("Product already in warehouse. For method: " + methodKey);
//    - 400 ---> NoSpecifiedProductInWarehouseException("Product not exist in warehouse yet. For method: " + methodKey);
//    ----- delivery:
//    - 404 ---> NoDeliveryFoundException("Delivery is not found. For method: " + methodKey);
//    ----- order:
//    - 400 ---> NoOrderFoundException("Order is not found. For method: " + methodKey);
//    ----- payment:
//    - 400 ---> NotEnoughInfoInOrderToCalculateException("Information in order is not enough to calculate payment. For method: " + methodKey);

}
