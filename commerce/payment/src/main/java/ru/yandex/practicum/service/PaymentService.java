package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.dto.PaymentDto;

public interface PaymentService {

    PaymentDto createNewPayment(OrderDto addedOrder);

    Double calculateProductsCostInOrder(OrderDto order);

    Double calculateOrderTotalCost(OrderDto order);

    PaymentDto makePaymentSuccess(String paymentId);

    PaymentDto makePaymentFailed(String paymentId);

}
