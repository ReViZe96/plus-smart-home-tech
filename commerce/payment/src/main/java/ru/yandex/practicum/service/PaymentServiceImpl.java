package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.dto.PaymentDto;
import ru.yandex.practicum.mapper.PaymentMapper;
import ru.yandex.practicum.repository.PaymentRepository;

@Component
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;


    //если недостаточно информации в заказе для расчёта (400) - NotEnoughInfoInOrderToCalculateException
    @Override
    public PaymentDto createNewPayment(OrderDto addedOrder) {
        return PaymentDto.builder().build();
    }

    //если недостаточно информации в заказе для расчёта (400) - NotEnoughInfoInOrderToCalculateException
    @Override
    public Double calculateProductsCostInOrder(OrderDto order) {
        return 0.0;
    }

    //если недостаточно информации в заказе для расчёта (400) - NotEnoughInfoInOrderToCalculateException
    @Override
    public Double calculateOrderTotalCost(OrderDto order) {
        return 0.0;
    }

    //возвращаемый тип может быть boolean или void
    //если не найден заказ (404) - NoOrderFoundException
    @Override
    public PaymentDto makePaymentSuccess(String paymentId) {
        return PaymentDto.builder().build();
    }

    //возвращаемый тип может быть boolean или void
    //если не найден заказ (404) - NoOrderFoundException
    @Override
    public PaymentDto makePaymentFailed(String paymentId) {
        return PaymentDto.builder().build();
    }

}
