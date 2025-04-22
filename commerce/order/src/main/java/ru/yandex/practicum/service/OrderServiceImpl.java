package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.clients.DeliveryClient;
import ru.yandex.practicum.clients.PaymentClient;
import ru.yandex.practicum.clients.WarehouseClient;
import ru.yandex.practicum.dto.*;
import ru.yandex.practicum.dto.request.CreateNewOrderRequest;
import ru.yandex.practicum.dto.request.ProductReturnRequest;
import ru.yandex.practicum.exception.NoOrderFoundException;
import ru.yandex.practicum.exception.NotAuthorizedUserException;
import ru.yandex.practicum.mapper.OrderMapper;
import ru.yandex.practicum.model.Order;
import ru.yandex.practicum.model.OrderState;
import ru.yandex.practicum.repository.OrderRepository;

import java.util.Optional;
import java.util.UUID;


@Service
@Slf4j
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    @Value("{order.default.warehouse.from}")
    private final String warehouseFromId;

    @Value("{order.default.warehouse.to}")
    private final String warehouseToId;

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final DeliveryClient deliveryClient;
    private final PaymentClient paymentClient;
    private final WarehouseClient warehouseClient;


    @Override
    public Page<OrderDto> getUserOrders(String username, Pageable pageable) {
        checkUsername(username);
        return orderRepository.findAll(pageable).map(orderMapper::orderToOrderDto);
    }

    @Override
    public OrderDto createNewOrder(CreateNewOrderRequest newOrderRequest) {
        ShoppingCartDto newCart = newOrderRequest.getShoppingCart();
        log.debug("Проверка наличия добавляемого в корзину товара в нужном количестве на складе - вызов внешнего сервиса");
        BookedProductsDto bookedProducts = warehouseClient.checkProductAmountInWarehouse(newCart);
        log.debug("Старт создания нового заказа по корзине {}", newCart.getShoppingCartId());
        Order simpleOrder = orderRepository.save(createSimpleOrder(newOrderRequest, bookedProducts));
        String simpleOrderId = simpleOrder.getOrderId().toString();

        log.debug("Старт создания доставки для нового заказа {}", simpleOrderId);
        DeliveryDto simpleDelivery = DeliveryDto.builder()
                .fromAddress(warehouseClient.getWarehouseAddress(warehouseFromId))
                .toAddress(warehouseClient.getWarehouseAddress(warehouseToId))
                .orderId(simpleOrderId)
                .build();
        log.debug("Создание доставки для заказа {} - вызов внешнего сервиса", simpleOrder);
        DeliveryDto newDelivery = deliveryClient.createNewDelivery(simpleDelivery);
        log.debug("Старт подготовки доставки {} - вызов внешнего сервиса", newDelivery.getDeliveryId());
        newDelivery = deliveryClient.makeDeliveryInProgress(newDelivery.getDeliveryId());
        simpleOrder.setDeliveryWeight(newDelivery.getWeigh());
        simpleOrder.setDeliveryVolume(newDelivery.getVolume());
        simpleOrder.setFragile(newDelivery.getFragile());
        orderRepository.save(simpleOrder);
        calculateOrderDeliveryCost(simpleOrderId);
        calculateOrderProductsCost(simpleOrderId);
        OrderDto complexOrder = calculateOrderTotalCost(simpleOrderId);

        log.debug("Старт создания платежа для нового заказа {}", complexOrder.getOrderId());
        PaymentDto orderPayment = paymentClient.createNewPayment(complexOrder);
        complexOrder.setPaymentId(orderPayment.getPaymentId());
        orderRepository.save(orderMapper.orderDtoToOrder(complexOrder));
        return complexOrder;
    }

    @Override
    public OrderDto returnOrder(ProductReturnRequest productReturnRequest, ProductReturnRequest returnRequest) {
        boolean isAllReturned = warehouseClient.returnProductsToWarehouse(returnRequest.getProducts());
        if (!isAllReturned) {
            log.warn("На склад возвращены не все товары из заявки на возврат");
        }
        Order returningOrder = checkOrder(returnRequest.getOrderId());
        returningOrder.setState(OrderState.PRODUCT_RETURNED);
        return orderMapper.orderToOrderDto(orderRepository.save(returningOrder));
    }

    @Override
    public OrderDto payOrder(String orderId) {
        Order paidOrder = checkOrder(UUID.fromString(orderId));
        return setOrderStateAndSave(paidOrder, OrderState.PAID);
    }

    @Override
    public OrderDto payOrderFailed(String orderId) {
        Order payingFailedOrder = checkOrder(UUID.fromString(orderId));
        return setOrderStateAndSave(payingFailedOrder, OrderState.PAYMENT_FAILED);
    }

    @Override
    public OrderDto assemblyOrder(String orderId) {
        Order assembledOrder = checkOrder(UUID.fromString(orderId));
        return setOrderStateAndSave(assembledOrder, OrderState.ASSEMBLED);
    }

    @Override
    public OrderDto assemblyOrderFailed(String orderId) {
        Order assemblingFailedOrder = checkOrder(UUID.fromString(orderId));
        return setOrderStateAndSave(assemblingFailedOrder, OrderState.ASSEMBLY_FAILED);
    }

    @Override
    public OrderDto deliveryOrder(String orderId) {
        Order deliveredOrder = checkOrder(UUID.fromString(orderId));
        return setOrderStateAndSave(deliveredOrder, OrderState.DELIVERED);
    }

    @Override
    public OrderDto deliveryOrderFailed(String orderId) {
        Order deliveringFailedOrder = checkOrder(UUID.fromString(orderId));
        return setOrderStateAndSave(deliveringFailedOrder, OrderState.DELIVERY_FAILED);
    }

    @Override
    public OrderDto completeOrder(String orderId) {
        Order completedOrder = checkOrder(UUID.fromString(orderId));
        return setOrderStateAndSave(completedOrder, OrderState.COMPLETED);
    }

    @Override
    public OrderDto calculateOrderTotalCost(String orderId) {
        Order order = checkOrder(UUID.fromString(orderId));
        log.debug("Расчёт общей стоимости заказа {} - вызов внешнего сервиса", orderId);
        Double totalPrice = paymentClient.calculateOrderTotalCost(orderMapper.orderToOrderDto(order));
        order.setTotalPrice(totalPrice);
        return orderMapper.orderToOrderDto(orderRepository.save(order));
    }

    @Override
    public OrderDto calculateOrderDeliveryCost(String orderId) {
        Order order = checkOrder(UUID.fromString(orderId));
        log.debug("Расчёт стоимости доставки заказа {} - вызов внешнего сервиса", orderId);
        Double deliveryPrice = deliveryClient.calculateDeliveryCost(orderMapper.orderToOrderDto(order));
        order.setDeliveryPrice(deliveryPrice);
        return orderMapper.orderToOrderDto(orderRepository.save(order));
    }

    @Override
    public OrderDto getByPaymentId(String paymentId) {
        log.debug("Старт поиска заказа по связанному с ним платежу {}", paymentId);
        return orderMapper.orderToOrderDto(orderRepository.findByPaymentId(paymentId).get());
    }

    @Override
    public OrderDto getById(String id) {
        return orderMapper.orderToOrderDto(checkOrder(UUID.fromString(id)));
    }


    private OrderDto calculateOrderProductsCost(String orderId) {
        Order order = checkOrder(UUID.fromString(orderId));
        log.debug("Расчёт стоимости товаров в заказе {} - вызов внешнего сервиса", order.getOrderId());
        Double productPrice = paymentClient.calculateProductsCostInOrder(orderMapper.orderToOrderDto(order));
        order.setProductPrice(productPrice);
        return orderMapper.orderToOrderDto(orderRepository.save(order));
    }

    private void checkUsername(String username) {
        if (username.isBlank()) {
            throw new NotAuthorizedUserException("Пользователя с именем: " + username + " не существует");
        }
    }

    private Order checkOrder(UUID orderId) {
        Optional<Order> order = orderRepository.findById(orderId);
        if (order.isEmpty()) {
            throw new NoOrderFoundException("Заказ с id = " + orderId + " не найден в системе");
        }
        return order.get();
    }

    private Order createSimpleOrder(CreateNewOrderRequest newOrderRequest, BookedProductsDto bookedProducts) {
        Order result = new Order();
        result.setShoppingCartId(newOrderRequest.getShoppingCart().getShoppingCartId());
        result.setProducts(newOrderRequest.getShoppingCart().getProducts());
        result.setState(OrderState.NEW);
        return result;
    }

    private OrderDto setOrderStateAndSave(Order order, OrderState state) {
        log.debug("Старт изменения статуса заказа {} на '{}'", order.getOrderId(), state.toString());
        order.setState(state);
        return orderMapper.orderToOrderDto(orderRepository.save(order));
    }

}
