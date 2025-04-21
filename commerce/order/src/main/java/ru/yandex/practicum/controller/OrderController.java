package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.dto.request.CreateNewOrderRequest;
import ru.yandex.practicum.dto.request.ProductReturnRequest;
import ru.yandex.practicum.service.OrderService;


@RestController("/api/v1/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;


    /**
     * Получение списка заказов пользователя в пагинированном виде.
     *
     * @param username имя пользователя
     * @return список всех заказов пользователя в пагинированном виде
     */
    @GetMapping
    public ResponseEntity<Page<OrderDto>> getUserOrders(@RequestParam(value = "username", required = true) String username,
                                                        @RequestParam(value = "pageable") Pageable pageable) {
        return ResponseEntity.ok(orderService.getUserOrders(username, pageable));
    }

    /**
     * Создание нового заказа в системе.
     *
     * @param newOrderRequest запрос на создание заказа
     */
    @PutMapping
    public ResponseEntity<OrderDto> createNewOrder(@RequestBody(required = true) CreateNewOrderRequest newOrderRequest) {
        return ResponseEntity.ok(orderService.createNewOrder(newOrderRequest));
    }

    /**
     * Возврат заказа.
     *
     * @param productReturnRequest запрос на возврат заказа
     * @return заказ пользователя после сборки
     */
    @PostMapping("/return")
    public ResponseEntity<OrderDto> returnOrder(@RequestParam(value = "productReturnRequest", required = true) ProductReturnRequest productReturnRequest,
                                                @RequestBody(required = true) ProductReturnRequest returnRequest) {
        return ResponseEntity.ok(orderService.returnOrder(productReturnRequest, returnRequest));
    }

    /**
     * Оплата заказа.
     *
     * @param orderId идентификатор заказа
     * @return заказ пользователя после оплаты
     */
    @PostMapping("/payment")
    public ResponseEntity<OrderDto> payOrder(@RequestBody(required = true) String orderId) {
        return ResponseEntity.ok(orderService.payOrder(orderId));
    }

    /**
     * Оплата заказа произошла с ошибкой.
     *
     * @param orderId идентификатор заказа
     * @return заказ пользователя после ошибки оплаты
     */
    @PostMapping("/payment/failed")
    public ResponseEntity<OrderDto> payOrderFailed(@RequestBody(required = true) String orderId) {
        return ResponseEntity.ok(orderService.payOrderFailed(orderId));
    }

    /**
     * Сборка заказа.
     *
     * @param orderId идентификатор заказа
     * @return заказ пользователя после сборки
     */
    @PostMapping("/assembly")
    public ResponseEntity<OrderDto> assemblyOrder(@RequestBody(required = true) String orderId) {
        return ResponseEntity.ok(orderService.assemblyOrder(orderId));
    }

    /**
     * Сборка заказа произошла с ошибкой.
     *
     * @param orderId идентификатор заказа
     * @return заказ пользователя после ошибки сборки
     */
    @PostMapping("/assembly/failed")
    public ResponseEntity<OrderDto> assemblyOrderFailed(@RequestBody(required = true) String orderId) {
        return ResponseEntity.ok(orderService.assemblyOrderFailed(orderId));
    }

    /**
     * Доставка заказа.
     *
     * @param orderId идентификатор заказа
     * @return заказ пользователя после доставки
     */
    @PostMapping("/delivery")
    public ResponseEntity<OrderDto> deliveryOrder(@RequestBody(required = true) String orderId) {
        return ResponseEntity.ok(orderService.deliveryOrder(orderId));
    }

    /**
     * Доставка заказа произошла с ошибкой.
     *
     * @param orderId идентификатор заказа
     * @return заказ пользователя после ошибки доставки
     */
    @PostMapping("/delivery/failed")
    public ResponseEntity<OrderDto> deliveryOrderFailed(@RequestBody(required = true) String orderId) {
        return ResponseEntity.ok(orderService.deliveryOrderFailed(orderId));
    }

    /**
     * Завершение заказа.
     *
     * @param orderId идентификатор заказа
     * @return завершенный заказ пользователя после всех стадий
     */
    @PostMapping("/completed")
    public ResponseEntity<OrderDto> completeOrder(@RequestBody(required = true) String orderId) {
        return ResponseEntity.ok(orderService.completeOrder(orderId));
    }

    /**
     * Расчёт общей стоимости заказа.
     *
     * @param orderId идентификатор заказа
     * @return заказ пользователя с расчётом общей стоимости
     */
    @PostMapping("/calculate/total")
    public ResponseEntity<OrderDto> calculateOrderTotalCost(@RequestBody(required = true) String orderId) {
        return ResponseEntity.ok(orderService.calculateOrderTotalCost(orderId));
    }

    /**
     * Расчёт стоимости доставки заказа.
     *
     * @param orderId идентификатор заказа
     * @return заказ пользователя с расчётом стоимости доставки
     */
    @PostMapping("/calculate/delivery")
    public ResponseEntity<OrderDto> calculateOrderDeliveryCost(@RequestBody(required = true) String orderId) {
        return ResponseEntity.ok(orderService.calculateOrderDeliveryCost(orderId));
    }

    /**
     * Получение сведений о заказе по идентификатору платежа за данный заказ.
     *
     * @param paymentId идентификатор платежа за заказ в БД.
     * @return Актуальный заказ со всеми сведениями из БД
     */
    @GetMapping("/byPaymentId/{paymentId}")
    public ResponseEntity<OrderDto> getOrderByPaymentId(@PathVariable(name = "paymentId") String paymentId) {
        return ResponseEntity.ok(orderService.getByPaymentId(paymentId));
    }

    /**
     * Получение сведений о заказе по идентификатору.
     *
     * @param id идентификатор заказа в БД.
     * @return Актуальный заказ со всеми сведениями из БД
     */
    @GetMapping("/byId/{id}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable(name = "id") String id) {
        return ResponseEntity.ok(orderService.getById(id));
    }

}
