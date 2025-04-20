package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.dto.PaymentDto;
import ru.yandex.practicum.service.PaymentService;

@RestController("/api/v1/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;


    /**
     * Формирование оплаты для заказа (переход в платежный шлюз).
     *
     * @param order заказ для формирования оплаты
     * @return сформированная оплата заказа
     */
    @PostMapping("")
    public ResponseEntity<PaymentDto> createNewPayment(@RequestBody(required = true) OrderDto order) {
        return ResponseEntity.ok(paymentService.createNewPayment(order));
    }

    /**
     * Расчёт стоимости товаров в заказе.
     *
     * @param order заказ для расчёта
     * @return стоимость товаров в заказе
     */
    @PostMapping("/productCost")
    public ResponseEntity<Double> calculateProductsCostInOrder(@RequestBody(required = true) OrderDto order) {
        return ResponseEntity.ok(paymentService.calculateProductsCostInOrder(order));
    }

    /**
     * Расчёт полной стоимости заказа.
     * Полная стоимость = рассчитанная стоимость товаров + стоимость доставки + налог
     *
     * @param order заказ для расчёта
     * @return полная стоимость заказа
     */
    @PostMapping("/totalCost")
    public ResponseEntity<Double> calculateOrderTotalCost(@RequestBody(required = true) OrderDto order) {
        return ResponseEntity.ok(paymentService.calculateOrderTotalCost(order));
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
    @PostMapping("/refund")
    public ResponseEntity<PaymentDto> makePaymentSuccess(@RequestBody(required = true) String paymentId) {
        return ResponseEntity.ok(paymentService.makePaymentSuccess(paymentId));
    }

    /**
     * Эмуляция отказа в оплате в платежном шлюзе.
     * Установка признака отказа в оплате.
     * Здесь принцип и логика такие же, как в методе makePaymentSuccess, только статус оплаты изменяется на FAILED.
     *
     * @param paymentId идентификатор платежа
     * @return сформированная оплата заказа
     */
    @PostMapping("/failed")
    public ResponseEntity<PaymentDto> makePaymentFailed(@RequestBody(required = true) String paymentId) {
        return ResponseEntity.ok(paymentService.makePaymentFailed(paymentId));
    }

}
