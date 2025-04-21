package ru.yandex.practicum.clients.fallback;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import ru.yandex.practicum.clients.OrderClient;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.dto.request.CreateNewOrderRequest;
import ru.yandex.practicum.dto.request.ProductReturnRequest;

import java.util.List;

@Component
@Slf4j
public class OrderClientFallback implements OrderClient {

    private static final String SERVICE_UNAVAILABLE = "Сервис 'Управление заказами' временно недоступен: ";
    static final OrderDto ORDER_STUB = OrderDto.builder()
            .orderId("stubId")
            .build();


    @Override
    public List<OrderDto> getUserOrders(String username, Pageable pageable) {
        log.warn(SERVICE_UNAVAILABLE + "невозможно получить список заказов пользователя {}.", username);
        return List.of(ORDER_STUB);
    }

    @Override
    public OrderDto createNewOrder(CreateNewOrderRequest newOrderRequest) {
        log.warn(SERVICE_UNAVAILABLE + "невозможно создать новый заказ по корзине: {}",
                newOrderRequest.getShoppingCart().getShoppingCartId());
        return ORDER_STUB;
    }

    @Override
    public OrderDto returnOrder(ProductReturnRequest productReturnRequest, ProductReturnRequest returnRequest) {
        log.warn(SERVICE_UNAVAILABLE + "невозможно осуществить возврат по заказу {}",
                productReturnRequest.getOrderId());
        return ORDER_STUB;
    }

    @Override
    public OrderDto payOrder(String orderId) {
        log.warn(SERVICE_UNAVAILABLE + "невозможно изменить статус заказа {} на 'Оплачен'", orderId);
        return ORDER_STUB;
    }

    @Override
    public OrderDto payOrderFailed(String orderId) {
        log.warn(SERVICE_UNAVAILABLE + "невозможно изменить статус заказа {} на 'Оплачен с ошибкой'", orderId);
        return ORDER_STUB;
    }

    @Override
    public OrderDto assemblyOrder(String orderId) {
        log.warn(SERVICE_UNAVAILABLE + "невозможно изменить статус заказа {} на 'Собран'", orderId);
        return ORDER_STUB;
    }

    @Override
    public OrderDto assemblyOrderFailed(String orderId) {
        log.warn(SERVICE_UNAVAILABLE + "невозможно изменить статус заказа {} на 'Собран с ошибкой'", orderId);
        return ORDER_STUB;
    }

    @Override
    public OrderDto deliveryOrder(String orderId) {
        log.warn(SERVICE_UNAVAILABLE + "невозможно изменить статус заказа {} на 'Доставлен'", orderId);
        return ORDER_STUB;
    }

    @Override
    public OrderDto deliveryOrderFailed(String orderId) {
        log.warn(SERVICE_UNAVAILABLE + "невозможно изменить статус заказа {} на 'Доставлен с ошибкой'", orderId);
        return ORDER_STUB;
    }

    @Override
    public OrderDto completeOrder(String orderId) {
        log.warn(SERVICE_UNAVAILABLE + "невозможно изменить статус заказа {} на 'Завершён'", orderId);
        return ORDER_STUB;
    }

    @Override
    public OrderDto calculateOrderTotalCost(String orderId) {
        log.warn(SERVICE_UNAVAILABLE + "невозможно расчитать общую стоимость заказа {}", orderId);
        return ORDER_STUB;
    }

    @Override
    public OrderDto calculateOrderDeliveryCost(String orderId) {
        log.warn(SERVICE_UNAVAILABLE + "невозможно расчитать стоимость доставки заказа {}", orderId);
        return ORDER_STUB;
    }

    @Override
    public OrderDto getOrderByPaymentId(String paymentId) {
        log.warn(SERVICE_UNAVAILABLE + "невозможно получить сведения о заказе по идентификатору платежа {}",
                paymentId);
        return ORDER_STUB;
    }

    @Override
    public OrderDto getOrderById(@PathVariable(name = "id") String id) {
        log.warn(SERVICE_UNAVAILABLE + "невозможно получить сведения о заказе по его идентификатору {}.", id);
        return ORDER_STUB;
    }

}
