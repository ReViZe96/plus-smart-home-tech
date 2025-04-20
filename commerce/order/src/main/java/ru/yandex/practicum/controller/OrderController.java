package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.dto.request.CreateNewOrderRequest;
import ru.yandex.practicum.dto.request.ProductReturnRequest;

import java.util.List;

@RestController("/api/v1/order")
@RequiredArgsConstructor
public class OrderController {

    //private final OrderService orderService;


    /**
     * Получение списка заказов пользователя в пагинированном виде.
     *
     * @param username имя пользователя
     * @return список всех заказов пользователя в пагинированном виде
     */
    //если пользователь не передан, либо не идентифицирован (401) - NotAuthorizedUserException
    @GetMapping
    public List<OrderDto> getUserOrders(@RequestParam(value = "username", required = true) String username,
                                        @RequestParam(value = "pageable") Pageable pageable) {
        return List.of(OrderDto.builder().build());
    }

    /**
     * Создание нового заказа в системе.
     *
     * @param newOrderRequest запрос на создание заказа
     */
    //если нет заказываемого товара на складе (400) - NoSpecifiedProductInWarehouseException
    @PutMapping
    public OrderDto createNewOrder(@RequestBody(required = true) CreateNewOrderRequest newOrderRequest) {
        return OrderDto.builder().build();
    }

    /**
     * Возврат заказа.
     *
     * @param productReturnRequest запрос на возврат заказа
     * @return заказ пользователя после сборки
     */
    //если не найден заказ (400) - NoOrderFoundException
    @PostMapping("/return")
    public OrderDto returnOrder(@RequestParam(value = "productReturnRequest", required = true) ProductReturnRequest productReturnRequest,
                                @RequestBody(required = true) ProductReturnRequest returnRequest) {
        return OrderDto.builder().build();
    }

    /**
     * Оплата заказа.
     *
     * @param orderId идентификатор заказа
     * @return заказ пользователя после оплаты
     */
    //если не найден заказ (400) - NoOrderFoundException
    @PostMapping("/payment")
    public OrderDto payOrder(@RequestBody(required = true) String orderId) {
        return OrderDto.builder().build();
    }

    /**
     * Оплата заказа произошла с ошибкой.
     *
     * @param orderId идентификатор заказа
     * @return заказ пользователя после ошибки оплаты
     */
    //если не найден заказ (400) - NoOrderFoundException
    @PostMapping("/payment/failed")
    public OrderDto payOrderFailed(@RequestBody(required = true) String orderId) {
        return OrderDto.builder().build();
    }

    /**
     * Сборка заказа.
     *
     * @param orderId идентификатор заказа
     * @return заказ пользователя после сборки
     */
    //если не найден заказ (400) - NoOrderFoundException
    @PostMapping("/assembly")
    public OrderDto assemblyOrder(@RequestBody(required = true) String orderId) {
        return OrderDto.builder().build();
    }

    /**
     * Сборка заказа произошла с ошибкой.
     *
     * @param orderId идентификатор заказа
     * @return заказ пользователя после ошибки сборки
     */
    //если не найден заказ (400) - NoOrderFoundException
    @PostMapping("/assembly/failed")
    public OrderDto assemblyOrderFailed(@RequestBody(required = true) String orderId) {
        return OrderDto.builder().build();
    }

    /**
     * Доставка заказа.
     *
     * @param orderId идентификатор заказа
     * @return заказ пользователя после доставки
     */
    //если не найден заказ (400) - NoOrderFoundException
    @PostMapping("/delivery")
    public OrderDto deliveryOrder(@RequestBody(required = true) String orderId) {
        return OrderDto.builder().build();
    }

    /**
     * Доставка заказа произошла с ошибкой.
     *
     * @param orderId идентификатор заказа
     * @return заказ пользователя после ошибки доставки
     */
    //если не найден заказ (400) - NoOrderFoundException
    @PostMapping("/delivery/failed")
    public OrderDto deliveryOrderFailed(@RequestBody(required = true) String orderId) {
        return OrderDto.builder().build();
    }

    /**
     * Завершение заказа.
     *
     * @param orderId идентификатор заказа
     * @return завершенный заказ пользователя после всех стадий
     */
    //если не найден заказ (400) - NoOrderFoundException
    @PostMapping("/completed")
    public OrderDto completeOrder(@RequestBody(required = true) String orderId) {
        return OrderDto.builder().build();
    }

    /**
     * Расчёт общей стоимости заказа.
     *
     * @param orderId идентификатор заказа
     * @return заказ пользователя с расчётом общей стоимости
     */
    //если не найден заказ (400) - NoOrderFoundException
    @PostMapping("/calculate/total")
    public OrderDto calculateOrderTotalCost(@RequestBody(required = true) String orderId) {
        return OrderDto.builder().build();
    }

    /**
     * Расчёт стоимости доставки заказа.
     *
     * @param orderId идентификатор заказа
     * @return заказ пользователя с расчётом стоимости доставки
     */
    //если не найден заказ (400) - NoOrderFoundException
    @PostMapping("/calculate/delivery")
    public OrderDto calculateOrderDeliveryCost(@RequestBody(required = true) String orderId) {
        return OrderDto.builder().build();
    }

}
