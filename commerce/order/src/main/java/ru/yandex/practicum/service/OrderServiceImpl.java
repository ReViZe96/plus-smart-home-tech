package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.dto.request.CreateNewOrderRequest;
import ru.yandex.practicum.dto.request.ProductReturnRequest;
import ru.yandex.practicum.mapper.OrderMapper;
import ru.yandex.practicum.repository.OrderRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    //если пользователь не передан, либо не идентифицирован (401) - NotAuthorizedUserException
    @Override
    public List<OrderDto> getUserOrders(String username, Pageable pageable) {
        return List.of(OrderDto.builder().build());
    }

    //если нет заказываемого товара на складе (400) - NoSpecifiedProductInWarehouseException
    @Override
    public OrderDto createNewOrder(CreateNewOrderRequest newOrderRequest) {
        return OrderDto.builder().build();
    }

    //если не найден заказ (400) - NoOrderFoundException
    @Override
    public OrderDto returnOrder(ProductReturnRequest productReturnRequest, ProductReturnRequest returnRequest) {
        return OrderDto.builder().build();
    }

    //если не найден заказ (400) - NoOrderFoundException
    @Override
    public OrderDto payOrder(String orderId) {
        return OrderDto.builder().build();
    }

    //если не найден заказ (400) - NoOrderFoundException
    @Override
    public OrderDto payOrderFailed(String orderId) {
        return OrderDto.builder().build();
    }

    //если не найден заказ (400) - NoOrderFoundException
    @Override
    public OrderDto assemblyOrder(String orderId) {
        return OrderDto.builder().build();
    }

    //если не найден заказ (400) - NoOrderFoundException
    @Override
    public OrderDto assemblyOrderFailed(String orderId) {
        return OrderDto.builder().build();
    }

    //если не найден заказ (400) - NoOrderFoundException
    @Override
    public OrderDto deliveryOrder(String orderId) {
        return OrderDto.builder().build();
    }

    //если не найден заказ (400) - NoOrderFoundException
    @Override
    public OrderDto deliveryOrderFailed(String orderId) {
        return OrderDto.builder().build();
    }

    //если не найден заказ (400) - NoOrderFoundException
    @Override
    public OrderDto completeOrder(String orderId) {
        return OrderDto.builder().build();
    }

    //если не найден заказ (400) - NoOrderFoundException
    @Override
    public OrderDto calculateOrderTotalCost(String orderId) {
        return OrderDto.builder().build();
    }

    //если не найден заказ (400) - NoOrderFoundException
    @Override
    public OrderDto calculateOrderDeliveryCost(String orderId) {
        return OrderDto.builder().build();
    }

}
