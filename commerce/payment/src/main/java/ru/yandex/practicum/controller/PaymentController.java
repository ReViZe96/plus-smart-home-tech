package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.dto.PaymentDto;

@RestController("/api/v1/payment")
@RequiredArgsConstructor
public class PaymentController {

    //private final PaymentService paymentService;


    /**
     * Формирование оплаты для заказа (переход в платежный шлюз).
     *
     * @param order заказ для формирования оплаты
     * @return сформированная оплата заказа
     */
    //если недостаточно информации в заказе для расчёта (400) - NotEnoughInfoInOrderToCalculateException
    @PostMapping("")
    public PaymentDto createNewPayment(@RequestBody(required = true) OrderDto order) {
        return PaymentDto.builder().build();
    }

    /**
     * Расчёт стоимости товаров в заказе.
     *
     * @param order заказ для расчёта
     * @return стоимость товаров в заказе
     */
    //если недостаточно информации в заказе для расчёта (400) - NotEnoughInfoInOrderToCalculateException
    @PostMapping("/productCost")
    public Double calculateProductsCostInOrder(@RequestBody(required = true) OrderDto order) {
        return 0.0;
    }

    /**
     * Расчёт полной стоимости заказа.
     * Полная стоимость = рассчитанная стоимость товаров + стоимость доставки + налог
     *
     * @param order заказ для расчёта
     * @return полная стоимость заказа
     */
    //если недостаточно информации в заказе для расчёта (400) - NotEnoughInfoInOrderToCalculateException
    @PostMapping("/totalCost")
    public Double calculateOrderTotalCost(@RequestBody(required = true) OrderDto order) {
        return 0.0;
    }

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
    //возвращаемый тип может быть boolean или void
    //если не найден заказ (404) - NoOrderFoundException
    @PostMapping("/refund")
    public PaymentDto makePaymentSuccess(@RequestBody(required = true) String paymentId) {
        return PaymentDto.builder().build();
    }

    /**
     * Эмуляция отказа в оплате в платежном шлюзе.
     * Установка признака отказа в оплате.
     * Здесь принцип и логика такие же, как в методе makePaymentSuccess, только статус оплаты изменяется на FAILED.
     *
     * @param paymentId идентификатор платежа
     * @return сформированная оплата заказа
     */
    //возвращаемый тип может быть boolean или void
    //если не найден заказ (404) - NoOrderFoundException
    @PostMapping("/failed")
    public PaymentDto makePaymentFailed(@RequestBody(required = true) String paymentId) {
        return PaymentDto.builder().build();
    }

}
