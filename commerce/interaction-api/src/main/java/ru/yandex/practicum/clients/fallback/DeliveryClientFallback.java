package ru.yandex.practicum.clients.fallback;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.clients.DeliveryClient;
import ru.yandex.practicum.dto.DeliveryDto;
import ru.yandex.practicum.dto.OrderDto;

@Component
@Slf4j
public class DeliveryClientFallback implements DeliveryClient {

    private static final String SERVICE_UNAVAILABLE = "Сервис 'Доставка' временно недоступен: ";
    static final DeliveryDto DELIVERY_STUB = DeliveryDto.builder()
            .deliveryId("stubId")
            .build();


    @Override
    public DeliveryDto createNewDelivery(DeliveryDto delivery) {
        log.warn(SERVICE_UNAVAILABLE + "невозможно создать новую заявку на доставку {}.", delivery.getDeliveryId());
        return DELIVERY_STUB;
    }

    @Override
    public DeliveryDto makeDeliveryInProgress(String deliveryId) {
        log.warn(SERVICE_UNAVAILABLE + "невозможно принять товары в доставку {}.", deliveryId);
        return DELIVERY_STUB;
    }

    @Override
    public DeliveryDto makeDeliverySuccess(String deliveryId) {
        log.warn(SERVICE_UNAVAILABLE + "невозможно проставить признак успешности доставке {}.", deliveryId);
        return DELIVERY_STUB;
    }

    @Override
    public DeliveryDto makeDeliveryFailed(String deliveryId) {
        log.warn(SERVICE_UNAVAILABLE + "невозможно проставить признак ошибки в доставке {}.", deliveryId);
        return DELIVERY_STUB;
    }

    @Override
    public Double calculateDeliveryCost(OrderDto order) {
        log.warn(SERVICE_UNAVAILABLE + "невозможно произвести расчёт полной стоимости доставки заказа {}.",
                order.getOrderId());
        return 0.0;
    }

}
