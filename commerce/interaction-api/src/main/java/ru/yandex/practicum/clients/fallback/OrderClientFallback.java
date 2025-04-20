package ru.yandex.practicum.clients.fallback;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.clients.OrderClient;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.dto.request.CreateNewOrderRequest;
import ru.yandex.practicum.dto.request.ProductReturnRequest;

import java.util.List;

@Component
@Slf4j
public class OrderClientFallback implements OrderClient {

    private static Logger logger = LoggerFactory.getLogger(OrderClientFallback.class);

    private static final String SERVICE_UNAVAILABLE = "Сервис 'Управление заказами' временно недоступен: ";
    static final OrderDto ORDER_STUB = OrderDto.builder()
            .orderId("stubId")
            .build();


    @Override
    public List<OrderDto> getUserOrders(String username, Pageable pageable) {
        logger.warn(SERVICE_UNAVAILABLE + "невозможно получить список заказов пользователя {}.", username);
        return List.of(ORDER_STUB);
    }

    @Override
    public OrderDto createNewOrder(CreateNewOrderRequest newOrderRequest) {
        logger.warn(SERVICE_UNAVAILABLE + "невозможно создать новый заказ по корзине: {}",
                newOrderRequest.getShoppingCart().getShoppingCartId());
        return ORDER_STUB;
    }

    @Override
    public OrderDto returnOrder(ProductReturnRequest productReturnRequest, ProductReturnRequest returnRequest) {
        logger.warn(SERVICE_UNAVAILABLE + "невозможно осуществить возврат по заказу {}",
                productReturnRequest.getOrderId());
        return ORDER_STUB;
    }

    @Override
    public OrderDto payOrder(String orderId) {
        logger.warn(SERVICE_UNAVAILABLE + "невозможно изменить статус заказа {} на 'Оплачен'", orderId);
        return ORDER_STUB;
    }

    @Override
    public OrderDto payOrderFailed(String orderId) {
        logger.warn(SERVICE_UNAVAILABLE + "невозможно изменить статус заказа {} на 'Оплачен с ошибкой'", orderId);
        return ORDER_STUB;
    }

    @Override
    public OrderDto assemblyOrder(String orderId) {
        logger.warn(SERVICE_UNAVAILABLE + "невозможно изменить статус заказа {} на 'Собран'", orderId);
        return ORDER_STUB;
    }

    @Override
    public OrderDto assemblyOrderFailed(String orderId) {
        logger.warn(SERVICE_UNAVAILABLE + "невозможно изменить статус заказа {} на 'Собран с ошибкой'", orderId);
        return ORDER_STUB;
    }

    @Override
    public OrderDto deliveryOrder(String orderId) {
        logger.warn(SERVICE_UNAVAILABLE + "невозможно изменить статус заказа {} на 'Доставлен'", orderId);
        return ORDER_STUB;
    }

    @Override
    public OrderDto deliveryOrderFailed(String orderId) {
        logger.warn(SERVICE_UNAVAILABLE + "невозможно изменить статус заказа {} на 'Доставлен с ошибкой'", orderId);
        return ORDER_STUB;
    }

    @Override
    public OrderDto completeOrder(String orderId) {
        logger.warn(SERVICE_UNAVAILABLE + "невозможно изменить статус заказа {} на 'Завершён'", orderId);
        return ORDER_STUB;
    }

    @Override
    public OrderDto calculateOrderTotalCost(String orderId) {
        logger.warn(SERVICE_UNAVAILABLE + "невозможно расчитать общую стоимость заказа {}", orderId);
        return ORDER_STUB;
    }

    @Override
    public OrderDto calculateOrderDeliveryCost(String orderId) {
        logger.warn(SERVICE_UNAVAILABLE + "невозможно расчитать стоимость доставки заказа {}", orderId);
        return ORDER_STUB;
    }

}
