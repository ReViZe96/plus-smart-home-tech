package ru.yandex.practicum.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.clients.fallback.PaymentClientFallback;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.dto.PaymentDto;

@FeignClient(name = "payment", fallback = PaymentClientFallback.class)
public interface PaymentClient {

    /**
     * Формирование оплаты для заказа (переход в платежный шлюз).
     *
     * @param order заказ для формирования оплаты
     * @return сформированная оплата заказа
     */
    @PostMapping("/api/v1/payment")
    PaymentDto createNewPayment(@RequestBody(required = true) OrderDto order);

    /**
     * Расчёт стоимости товаров в заказе.
     *
     * @param order заказ для расчёта
     * @return стоимость товаров в заказе
     */
    @PostMapping("/api/v1/payment/productCost")
    Double calculateProductsCostInOrder(@RequestBody(required = true) OrderDto order);

    /**
     * Расчёт полной стоимости заказа.
     * Полная стоимость = рассчитанная стоимость товаров + стоимость доставки + налог
     *
     * @param order заказ для расчёта
     * @return полная стоимость заказа
     */
    @PostMapping("/api/v1/payment/totalCost")
    Double calculateOrderTotalCost(@RequestBody(required = true) OrderDto order);

    /**
     * Эмуляция успешной оплаты в платежном шлюзе.
     * Установка признака успешности оплаты.
     * С помощью этого эндпоинта платёжный шлюз будет обновлять статус оплаты.
     * Его может вызывать внешняя система для влияния на нашу систему в случае успешного заказа.
     * Логика его работы:
     * a. найти и проверить, что идентификатор оплаты существует;
     * b. изменить статус на SUCCESS;
     * c. вызвать изменение в сервисе заказов — статус оплачен.
     *
     * @param paymentId идентификатор платежа
     * @return сформированная оплата заказа
     */
    @PostMapping("/api/v1/payment/refund")
    PaymentDto makePaymentSuccess(@RequestBody(required = true) String paymentId);

    /**
     * Эмуляция отказа в оплате в платежном шлюзе.
     * Установка признака отказа в оплате.
     * Здесь принцип и логика такие же, как в методе makePaymentSuccess, только статус оплаты изменяется на FAILED.
     *
     * @param paymentId идентификатор платежа
     * @return сформированная оплата заказа
     */
    @PostMapping("/api/v1/payment/failed")
    PaymentDto makePaymentFailed(@RequestBody(required = true) String paymentId);

}
