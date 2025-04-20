package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dto.DeliveryDto;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.mapper.DeliveryMapper;
import ru.yandex.practicum.repository.DeliveryRepository;

@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final DeliveryMapper deliveryMapper;

    @Override
    public DeliveryDto createNewDelivery(DeliveryDto addingDelivery) {
        return DeliveryDto.builder().build();
    }

    //возвращаемый тип может быть boolean или void
    //в случае, если доставка не найдена (404) - NoDeliveryFoundException
    @Override
    public DeliveryDto makeDeliveryInProgress(String deliveryId) {
        return DeliveryDto.builder().build();
    }

    //возвращаемый тип может быть boolean или void
    //в случае, если доставка не найдена (404) - NoDeliveryFoundException
    @Override
    public DeliveryDto makeDeliverySuccess(String deliveryId) {
        return DeliveryDto.builder().build();
    }

    //возвращаемый тип может быть boolean или void
    //в случае, если доставка не найдена (404) - NoDeliveryFoundException
    @Override
    public DeliveryDto makeDeliveryFailed(String deliveryId) {
        return DeliveryDto.builder().build();
    }

    //в случае, если доставка не найдена (404) - NoDeliveryFoundException
    @Override
    public Double calculateDeliveryCost(OrderDto order) {
        return 0.0;
    }

}
