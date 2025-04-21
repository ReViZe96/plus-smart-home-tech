package ru.yandex.practicum.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.clients.fallback.OrderClientFallback;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.dto.request.CreateNewOrderRequest;
import ru.yandex.practicum.dto.request.ProductReturnRequest;

import java.util.List;

@FeignClient(name = "order", fallback = OrderClientFallback.class)
public interface OrderClient {

    /**
     * Получение списка заказов пользователя в пагинированном виде.
     *
     * @param username имя пользователя
     * @return список всех заказов пользователя в пагинированном виде
     */
    @GetMapping("/api/v1/order")
    List<OrderDto> getUserOrders(@RequestParam(value = "username", required = true) String username,
                                 @RequestParam(value = "pageable") Pageable pageable);

    /**
     * Создание нового заказа в системе.
     *
     * @param newOrderRequest запрос на создание заказа
     */
    @PutMapping("/api/v1/order")
    OrderDto createNewOrder(@RequestBody(required = true) CreateNewOrderRequest newOrderRequest);

    /**
     * Возврат заказа.
     *
     * @param productReturnRequest запрос на возврат заказа
     * @return заказ пользователя после сборки
     */
    @PostMapping("/api/v1/order/return")
    OrderDto returnOrder(@RequestParam(value = "productReturnRequest", required = true) ProductReturnRequest productReturnRequest,
                         @RequestBody(required = true) ProductReturnRequest returnRequest);

    /**
     * Оплата заказа.
     *
     * @param orderId идентификатор заказа
     * @return заказ пользователя после оплаты
     */
    @PostMapping("/api/v1/order/payment")
    OrderDto payOrder(@RequestBody(required = true) String orderId);

    /**
     * Оплата заказа произошла с ошибкой.
     *
     * @param orderId идентификатор заказа
     * @return заказ пользователя после ошибки оплаты
     */
    @PostMapping("/api/v1/order/payment/failed")
    OrderDto payOrderFailed(@RequestBody(required = true) String orderId);

    /**
     * Сборка заказа.
     *
     * @param orderId идентификатор заказа
     * @return заказ пользователя после сборки
     */
    @PostMapping("/api/v1/order/assembly")
    OrderDto assemblyOrder(@RequestBody(required = true) String orderId);

    /**
     * Сборка заказа произошла с ошибкой.
     *
     * @param orderId идентификатор заказа
     * @return заказ пользователя после ошибки сборки
     */
    @PostMapping("/api/v1/order/assembly/failed")
    OrderDto assemblyOrderFailed(@RequestBody(required = true) String orderId);

    /**
     * Доставка заказа.
     *
     * @param orderId идентификатор заказа
     * @return заказ пользователя после доставки
     */
    @PostMapping("/api/v1/order/delivery")
    OrderDto deliveryOrder(@RequestBody(required = true) String orderId);

    /**
     * Доставка заказа произошла с ошибкой.
     *
     * @param orderId идентификатор заказа
     * @return заказ пользователя после ошибки доставки
     */
    @PostMapping("/api/v1/order/delivery/failed")
    OrderDto deliveryOrderFailed(@RequestBody(required = true) String orderId);

    /**
     * Завершение заказа.
     *
     * @param orderId идентификатор заказа
     * @return завершенный заказ пользователя после всех стадий
     */
    @PostMapping("/api/v1/order/completed")
    OrderDto completeOrder(@RequestBody(required = true) String orderId);

    /**
     * Расчёт общей стоимости заказа.
     *
     * @param orderId идентификатор заказа
     * @return заказ пользователя с расчётом общей стоимости
     */
    @PostMapping("/api/v1/order/calculate/total")
    OrderDto calculateOrderTotalCost(@RequestBody(required = true) String orderId);

    /**
     * Расчёт стоимости доставки заказа.
     *
     * @param orderId идентификатор заказа
     * @return заказ пользователя с расчётом стоимости доставки
     */
    @PostMapping("/api/v1/order/calculate/delivery")
    OrderDto calculateOrderDeliveryCost(@RequestBody(required = true) String orderId);

    /**
     * Получение сведений о заказе по идентификатору платежа за данный заказ.
     *
     * @param paymentId идентификатор платежа за заказ в БД.
     * @return Актуальный заказ со всеми сведениями из БД
     */
    @GetMapping("/api/v1/order/byPaymentId/{paymentId}")
    OrderDto getOrderByPaymentId(@PathVariable(name = "paymentId") String paymentId);

    /**
     * Получение сведений о заказе по идентификатору.
     *
     * @param id идентификатор заказа в БД.
     * @return Актуальный заказ со всеми сведениями из БД
     */
    @GetMapping("/api/v1/order/byId/{id}")
    OrderDto getOrderById(@PathVariable(name = "id") String id);

}
