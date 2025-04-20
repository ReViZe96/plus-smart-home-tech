package ru.yandex.practicum.service;

import org.springframework.data.domain.Pageable;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.dto.request.CreateNewOrderRequest;
import ru.yandex.practicum.dto.request.ProductReturnRequest;

import java.util.List;

public interface OrderService {

    List<OrderDto> getUserOrders(String username, Pageable pageable);

    OrderDto createNewOrder(CreateNewOrderRequest newOrderRequest);

    OrderDto returnOrder(ProductReturnRequest productReturnRequest, ProductReturnRequest returnRequest);

    OrderDto payOrder(String orderId);

    OrderDto payOrderFailed(String orderId);

    OrderDto assemblyOrder(String orderId);

    OrderDto assemblyOrderFailed(String orderId);

    OrderDto deliveryOrder(String orderId);

    OrderDto deliveryOrderFailed(String orderId);

    OrderDto completeOrder(String orderId);

    OrderDto calculateOrderTotalCost(String orderId);

    OrderDto calculateOrderDeliveryCost(String orderId);

}
