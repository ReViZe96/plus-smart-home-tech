package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.clients.OrderClient;
import ru.yandex.practicum.clients.ShoppingStoreClient;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.dto.PaymentDto;
import ru.yandex.practicum.exception.NoOrderFoundException;
import ru.yandex.practicum.exception.NotEnoughInfoInOrderToCalculateException;
import ru.yandex.practicum.mapper.PaymentMapper;
import ru.yandex.practicum.model.Payment;
import ru.yandex.practicum.model.PaymentStatus;
import ru.yandex.practicum.repository.PaymentRepository;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final OrderClient orderClient;
    private final ShoppingStoreClient storeClient;


    @Override
    public PaymentDto createNewPayment(OrderDto addedOrder) {
        Double productCost = addedOrder.getProductPrice();
        Double deliveryCost = addedOrder.getDeliveryPrice();
        Double totalCost = addedOrder.getTotalPrice();
        if (productCost == null || deliveryCost == null || totalCost == null) {
            throw new NotEnoughInfoInOrderToCalculateException("В заказе " + addedOrder.getOrderId() +
                    " недостаточно информации для расчёта и сохранения платежа");
        }
        log.debug("Старт создания платежа по заказу {}", addedOrder.getOrderId());
        Payment newPayment = new Payment();
        newPayment.setProductTotal(productCost);
        newPayment.setDeliveryTotal(deliveryCost);
        newPayment.setTotalPayment(totalCost);
        newPayment.setStatus(PaymentStatus.PENDING);
        newPayment.setFeeTotal(calculateOrderFee(productCost));
        return paymentMapper.paymentToPaymentDto(paymentRepository.save(newPayment));
    }

    @Override
    public Double calculateProductsCostInOrder(OrderDto order) {
        Map<String, Integer> products = order.getProducts();
        if (products == null || products.isEmpty()) {
            throw new NotEnoughInfoInOrderToCalculateException("В заказе " + order.getOrderId() +
                    " недостаточно информации для расчёта общей стоимости товаров");
        }
        double productsCost = 0.0;
        for (String productId : products.keySet()) {
            log.debug("Запрос информации о товаре {} - вызов внешнего сервиса", productId);
            productsCost += storeClient.getProductById(productId).getPrice() * products.get(productId);
        }
        log.debug("Общая стоимость товаров в заказе {} расчитана и равна {}", order.getOrderId(), productsCost);
        return productsCost;
    }

    @Override
    public Double calculateOrderTotalCost(OrderDto order) {
        Double productCost = order.getProductPrice();
        Double deliveryCost = order.getDeliveryPrice();
        Double fee;
        if (productCost == null || deliveryCost == null) {
            throw new NotEnoughInfoInOrderToCalculateException("В заказе " + order.getOrderId() +
                    " недостаточно информации для расчёта его стоимости");
        }
        fee = calculateOrderFee(productCost);
        log.debug("Старт расчёта полной стоимости заказа {}", order.getOrderId());
        return productCost + fee + deliveryCost;
    }

    @Override
    public PaymentDto makePaymentSuccess(String paymentId) {
        Payment successPayment = checkPayment(UUID.fromString(paymentId));
        PaymentDto successPaymentDto = setPaymentStatusAndSave(successPayment, PaymentStatus.SUCCESS);
        log.debug("Статус платёжа {} изменён на 'Успешно'", paymentId);
        OrderDto successOrder = isOrderExist(successPayment);
        log.debug("Старт изменения статуса заказа {}, связанного  с платежом {} на 'Оплачено' - вызов внешнего сервиса",
                successOrder.getOrderId(), paymentId);
        orderClient.payOrder(successOrder.getOrderId());
        return successPaymentDto;
    }

    @Override
    public PaymentDto makePaymentFailed(String paymentId) {
        Payment failedPayment = checkPayment(UUID.fromString(paymentId));
        PaymentDto failedPaymentDto = setPaymentStatusAndSave(failedPayment, PaymentStatus.FAILED);
        log.debug("Статус платёжа {} изменён на 'Неудачно'", paymentId);
        OrderDto failedOrder = isOrderExist(failedPayment);
        log.debug("Старт изменения статуса заказа {}, связанного  с платежом {} на 'Ошибка при оплате' - вызов внешнего сервиса",
                failedOrder.getOrderId(), paymentId);
        orderClient.payOrderFailed(failedOrder.getOrderId());
        return failedPaymentDto;
    }


    private Double calculateOrderFee(Double productCost) {
        return productCost * 0.1;
    }

    private Payment checkPayment(UUID paymentId) {
        Optional<Payment> payment = paymentRepository.findById(paymentId);
        if (payment.isEmpty()) {
            throw new NoOrderFoundException("Платёж с id = " + paymentId + " не найден в системе");
        }
        return paymentRepository.findById(paymentId).get();
    }

    private PaymentDto setPaymentStatusAndSave(Payment payment, PaymentStatus status) {
        payment.setStatus(status);
        return paymentMapper.paymentToPaymentDto(paymentRepository.save(payment));
    }

    private OrderDto isOrderExist(Payment payment) {
        OrderDto order = orderClient.getOrderByPaymentId(payment.getPaymentId().toString());
        if (order == null) {
            throw new NoOrderFoundException("Заказ, связанный с платежом " + payment.getPaymentId() + " не найден в системе");
        }
        return order;
    }

}
