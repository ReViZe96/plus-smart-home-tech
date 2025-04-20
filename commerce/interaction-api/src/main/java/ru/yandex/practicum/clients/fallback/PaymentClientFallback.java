package ru.yandex.practicum.clients.fallback;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.clients.PaymentClient;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.dto.PaymentDto;

@Component
@Slf4j
public class PaymentClientFallback implements PaymentClient {

    private static Logger logger = LoggerFactory.getLogger(PaymentClientFallback.class);

    private static final String SERVICE_UNAVAILABLE = "Сервис 'Оплата' временно недоступен: ";
    static final PaymentDto PAYMENT_STUB = PaymentDto.builder()
            .paymentId("stubId")
            .build();


    @Override
    public PaymentDto createNewPayment(OrderDto order) {
        logger.warn(SERVICE_UNAVAILABLE + "невозможно сформировать оплату для заказа {}", order.getOrderId());
        return PAYMENT_STUB;
    }

    @Override
    public Double calculateProductsCostInOrder(OrderDto order) {
        logger.warn(SERVICE_UNAVAILABLE + "невозможно расчитать стоимость товаров в заказе {}", order.getOrderId());
        return 0.0;
    }

    @Override
    public Double calculateOrderTotalCost(OrderDto order) {
        logger.warn(SERVICE_UNAVAILABLE + "невозможно расчитать полную стоимость заказа {}", order.getOrderId());
        return 0.0;
    }

    @Override
    public PaymentDto makePaymentSuccess(String paymentId) {
        logger.warn(SERVICE_UNAVAILABLE + "невозможно изменить статус оплаты {} на 'Успешно'", paymentId);
        return PAYMENT_STUB;
    }

    @Override
    public PaymentDto makePaymentFailed(String paymentId) {
        logger.warn(SERVICE_UNAVAILABLE + "невозможно изменить статус оплаты {} на 'Неудачно'", paymentId);
        return PAYMENT_STUB;
    }

}
