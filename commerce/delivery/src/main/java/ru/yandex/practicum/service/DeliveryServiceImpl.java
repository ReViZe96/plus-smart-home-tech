package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.clients.WarehouseClient;
import ru.yandex.practicum.dto.AddressDto;
import ru.yandex.practicum.dto.DeliveryDto;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.dto.request.ShippedToDeliveryRequest;
import ru.yandex.practicum.exception.NoDeliveryFoundException;
import ru.yandex.practicum.mapper.DeliveryMapper;
import ru.yandex.practicum.model.Delivery;
import ru.yandex.practicum.model.DeliveryState;
import ru.yandex.practicum.repository.DeliveryRepository;

import javax.validation.ValidationException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {

    private static Logger logger = LoggerFactory.getLogger(DeliveryServiceImpl.class);

    private final DeliveryRepository deliveryRepository;
    private final DeliveryMapper deliveryMapper;
    private final WarehouseClient warehouseClient;


    @Override
    public DeliveryDto createNewDelivery(DeliveryDto addingDelivery) {
        checkDelivery(addingDelivery);
        logger.debug("Старт создания новой доставки {}", addingDelivery.getDeliveryId());
        Delivery newDelivery = new Delivery();
        String stringFromAddress = addressToString(addingDelivery.getFromAddress());
        newDelivery.setFromAddress(stringFromAddress);
        String stringToAddress = addressToString(addingDelivery.getToAddress());
        newDelivery.setToAddress(stringToAddress);
        newDelivery.setOrderId(addingDelivery.getOrderId());
        newDelivery.setState(DeliveryState.CREATED);
        return deliveryMapper.deliveryToDeliveryDto(deliveryRepository.save(newDelivery));

    }

    @Override
    public DeliveryDto makeDeliveryInProgress(String deliveryId) {
        Optional<Delivery> delivery = deliveryRepository.findById(UUID.fromString(deliveryId));
        if (delivery.isEmpty()) {
            throw new NoDeliveryFoundException("Доставка с id = " + deliveryId + " не найдена в системе");
        }
        Delivery deliveryInWork = delivery.get();

        logger.debug("Старт формирования запроса к складу на передачу товаров заказа {} в рамках доставки {}",
                deliveryInWork.getOrderId(), deliveryInWork.getId());
        ShippedToDeliveryRequest shippedToDeliveryRequest = ShippedToDeliveryRequest
                .builder()
                .orderId(deliveryInWork.getOrderId())
                .deliveryId(deliveryInWork.getId())
                .build();
        logger.debug("Старт передачи товаров заказа {} в доставку {} - вызов внешнего сервиса",
                deliveryInWork.getOrderId(), deliveryInWork.getId());
        DeliveryDto assembledDelivery = warehouseClient.shippedProductsToDelivery(shippedToDeliveryRequest);
        deliveryInWork.setVolume(assembledDelivery.getVolume());
        deliveryInWork.setWeigh(assembledDelivery.getWeigh());
        deliveryInWork.setFragile(assembledDelivery.getFragile());
        deliveryInWork.setState(DeliveryState.IN_PROGRESS);
        return deliveryMapper.deliveryToDeliveryDto(deliveryRepository.save(deliveryInWork));
    }

    @Override
    public DeliveryDto makeDeliverySuccess(String deliveryId) {
        Delivery successDelivery = isDeliveryPresent(deliveryId);
        return setDeliveryStateAndSave(successDelivery, DeliveryState.DELIVERED);
    }

    @Override
    public DeliveryDto makeDeliveryFailed(String deliveryId) {
        Delivery failedDelivery = isDeliveryPresent(deliveryId);
        return setDeliveryStateAndSave(failedDelivery, DeliveryState.FAILED);
    }

    @Override
    public Double calculateDeliveryCost(OrderDto order) {
        Optional<Delivery> calculatingDelivery = deliveryRepository.findById(UUID.fromString(order.getDeliveryId()));
        double baseCost = 5.0;
        if (calculatingDelivery.isEmpty()) {
            throw new NoDeliveryFoundException("Доставка с id = " + order.getDeliveryId() + " не найдена в системе");
        }
        Delivery delivery = calculatingDelivery.get();
        if (delivery.getFromAddress().contains("ADDRESS_1")) {
            baseCost += baseCost * 1;
        } else if (delivery.getFromAddress().contains("ADDRESS_2")) {
            baseCost += baseCost * 2;
        }
        if (delivery.getFragile()) {
            logger.debug("Коэффициент 0.2 за доставку хрупокого товара");
            baseCost += baseCost * 0.2;
        }
        logger.debug("Коэффициент 0.3 для веса товара");
        baseCost += delivery.getWeigh() * 0.3;
        logger.debug("Коэффициент 0.2 для объема товара");
        baseCost += delivery.getVolume() * 0.2;
        if (delivery.getToAddress().split(",")[2].equals(delivery.getFromAddress().split(",")[2])) {
            return baseCost;
        } else {
            logger.debug("Коэффициент 0.2 для доставки за пределами одной и той же улицы.");
            baseCost += baseCost * 0.2;
            return baseCost;
        }
    }


    private void checkDelivery(DeliveryDto delivery) {
        if (deliveryRepository.findById(UUID.fromString(delivery.getDeliveryId())).isPresent()) {
            throw new ValidationException("Доставка с id = " + delivery.getDeliveryId() + " уже существует");
        }
        if (delivery.getFromAddress() == null || delivery.getToAddress() == null || delivery.getOrderId() == null) {
            throw new ValidationException("Недостаточно данных для создания новой доставки с id = " +
                    delivery.getDeliveryId());
        }
    }

    private String addressToString(AddressDto address) {
        String joiner = ",";
        return String.join(joiner, List.of(address.getCountry(), address.getCity(), address.getStreet(),
                address.getHouse(), address.getFlat()));
    }

    private Delivery isDeliveryPresent(String deliveryId) {
        Optional<Delivery> delivery = deliveryRepository.findById(UUID.fromString(deliveryId));
        if (delivery.isEmpty()) {
            throw new NoDeliveryFoundException("Доставка с id = " + deliveryId + " не найдена в системе");
        }
        return delivery.get();
    }

    private DeliveryDto setDeliveryStateAndSave(Delivery delivery, DeliveryState state) {
        logger.debug("Старт изменения статуса доставки {} на '{}'", delivery.getId(), state.toString());
        delivery.setState(state);
        return deliveryMapper.deliveryToDeliveryDto(deliveryRepository.save(delivery));
    }

}
