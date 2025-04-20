package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.DeliveryDto;
import ru.yandex.practicum.dto.OrderDto;

public interface DeliveryService {

    DeliveryDto createNewDelivery(DeliveryDto addingDelivery);

    DeliveryDto makeDeliveryInProgress(String deliveryId);

    DeliveryDto makeDeliverySuccess(String deliveryId);

    DeliveryDto makeDeliveryFailed(String deliveryId);

    Double calculateDeliveryCost(OrderDto order);

}
