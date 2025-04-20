package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.dto.request.CreateNewOrderRequest;
import ru.yandex.practicum.dto.request.ProductReturnRequest;
import ru.yandex.practicum.service.OrderService;

import java.util.List;

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
    public List<OrderDto> getUserOrders(@RequestParam(value = "username", required = true) String username,
                                        @RequestParam(value = "pageable") Pageable pageable) {
        return orderService.getUserOrders(username, pageable);
    }

    /**
     * Создание нового заказа в системе.
     *
     * @param newOrderRequest запрос на создание заказа
     */
    @PutMapping
    public OrderDto createNewOrder(@RequestBody(required = true) CreateNewOrderRequest newOrderRequest) {
        return orderService.createNewOrder(newOrderRequest);
    }

    /**
     * Возврат заказа.
     *
     * @param productReturnRequest запрос на возврат заказа
     * @return заказ пользователя после сборки
     */
    @PostMapping("/return")
    public OrderDto returnOrder(@RequestParam(value = "productReturnRequest", required = true) ProductReturnRequest productReturnRequest,
                                @RequestBody(required = true) ProductReturnRequest returnRequest) {
        return orderService.returnOrder(productReturnRequest, returnRequest);
    }

    /**
     * Оплата заказа.
     *
     * @param orderId идентификатор заказа
     * @return заказ пользователя после оплаты
     */
    @PostMapping("/payment")
    public OrderDto payOrder(@RequestBody(required = true) String orderId) {
        return orderService.payOrder(orderId);
    }

    /**
     * Оплата заказа произошла с ошибкой.
     *
     * @param orderId идентификатор заказа
     * @return заказ пользователя после ошибки оплаты
     */
    @PostMapping("/payment/failed")
    public OrderDto payOrderFailed(@RequestBody(required = true) String orderId) {
        return orderService.payOrderFailed(orderId);
    }

    /**
     * Сборка заказа.
     *
     * @param orderId идентификатор заказа
     * @return заказ пользователя после сборки
     */
    @PostMapping("/assembly")
    public OrderDto assemblyOrder(@RequestBody(required = true) String orderId) {
        return orderService.assemblyOrder(orderId);
    }

    /**
     * Сборка заказа произошла с ошибкой.
     *
     * @param orderId идентификатор заказа
     * @return заказ пользователя после ошибки сборки
     */
    @PostMapping("/assembly/failed")
    public OrderDto assemblyOrderFailed(@RequestBody(required = true) String orderId) {
        return orderService.assemblyOrderFailed(orderId);
    }

    /**
     * Доставка заказа.
     *
     * @param orderId идентификатор заказа
     * @return заказ пользователя после доставки
     */
    @PostMapping("/delivery")
    public OrderDto deliveryOrder(@RequestBody(required = true) String orderId) {
        return orderService.deliveryOrder(orderId);
    }

    /**
     * Доставка заказа произошла с ошибкой.
     *
     * @param orderId идентификатор заказа
     * @return заказ пользователя после ошибки доставки
     */
    @PostMapping("/delivery/failed")
    public OrderDto deliveryOrderFailed(@RequestBody(required = true) String orderId) {
        return orderService.deliveryOrderFailed(orderId);
    }

    /**
     * Завершение заказа.
     *
     * @param orderId идентификатор заказа
     * @return завершенный заказ пользователя после всех стадий
     */
    @PostMapping("/completed")
    public OrderDto completeOrder(@RequestBody(required = true) String orderId) {
        return orderService.completeOrder(orderId);
    }

    /**
     * Расчёт общей стоимости заказа.
     *
     * @param orderId идентификатор заказа
     * @return заказ пользователя с расчётом общей стоимости
     */
    @PostMapping("/calculate/total")
    public OrderDto calculateOrderTotalCost(@RequestBody(required = true) String orderId) {
        return orderService.calculateOrderTotalCost(orderId);
    }

    /**
     * Расчёт стоимости доставки заказа.
     *
     * @param orderId идентификатор заказа
     * @return заказ пользователя с расчётом стоимости доставки
     */
    @PostMapping("/calculate/delivery")
    public OrderDto calculateOrderDeliveryCost(@RequestBody(required = true) String orderId) {
        return orderService.calculateOrderDeliveryCost(orderId);
    }

}
