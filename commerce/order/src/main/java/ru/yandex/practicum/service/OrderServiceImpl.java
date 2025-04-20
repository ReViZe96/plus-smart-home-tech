package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.clients.DeliveryClient;
import ru.yandex.practicum.clients.PaymentClient;
import ru.yandex.practicum.clients.WarehouseClient;
import ru.yandex.practicum.dto.BookedProductsDto;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.dto.ShoppingCartDto;
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

    private static Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final WarehouseClient warehouseClient;
    private final DeliveryClient deliveryClient;
    private final PaymentClient paymentClient;

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
        Order simpleOrder = orderRepository.save(createSimpleOrder(newOrderRequest, bookedProducts));
        String simpleOrderId = simpleOrder.getOrderId().toString();
        calculateOrderDeliveryCost(simpleOrderId);
        calculateOrderProductsCost(simpleOrderId);
        return calculateOrderTotalCost(simpleOrderId);
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
        log.debug("Расчёт стоимости доставки заказа - вызов внешнего сервиса");
        Double deliveryPrice = deliveryClient.calculateDeliveryCost(orderMapper.orderToOrderDto(order));
        order.setDeliveryPrice(deliveryPrice);
        return orderMapper.orderToOrderDto(orderRepository.save(order));
    }


    private OrderDto calculateOrderProductsCost(String orderId) {
        Order order = checkOrder(UUID.fromString(orderId));
        log.debug("Расчёт стоимости продуктов в заказе - вызов внешнего сервиса");
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
        Optional<Order> returningOrder = orderRepository.findById(orderId);
        if (returningOrder.isEmpty()) {
            throw new NoOrderFoundException("Заказ с id = " + orderId + " не найден в системе");
        }
        return orderRepository.findById(orderId).get();
    }

    private Order createSimpleOrder(CreateNewOrderRequest newOrderRequest, BookedProductsDto bookedProducts) {
        Order result = new Order();
        result.setShoppingCartId(newOrderRequest.getShoppingCart().getShoppingCartId());
        result.setProducts(newOrderRequest.getShoppingCart().getProducts());
        result.setState(OrderState.ON_PAYMENT);
        result.setDeliveryWeight(bookedProducts.getDeliveryWeight());
        result.setDeliveryVolume(bookedProducts.getDeliveryVolume());
        result.setFragile(bookedProducts.getFragile());
        return result;
    }

    private OrderDto setOrderStateAndSave(Order order, OrderState state) {
        order.setState(state);
        return orderMapper.orderToOrderDto(orderRepository.save(order));
    }

}
