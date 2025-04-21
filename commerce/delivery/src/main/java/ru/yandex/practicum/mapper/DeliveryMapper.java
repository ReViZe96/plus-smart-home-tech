package ru.yandex.practicum.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.dto.AddressDto;
import ru.yandex.practicum.dto.DeliveryDto;
import ru.yandex.practicum.model.Delivery;

import java.util.List;

@Component
public class DeliveryMapper {

    private static final String JOINER = ",";

    public String addressDtoToaddress(AddressDto address) {
        return String.join(JOINER, List.of(address.getCountry(), address.getCity(), address.getStreet(),
                address.getHouse(), address.getFlat()));
    }

    public AddressDto addressToAdressDto(String address) {
        String[] addressParts = address.split(JOINER);
        return AddressDto.builder()
                .country(addressParts[0])
                .city(addressParts[1])
                .street(addressParts[2])
                .house(addressParts[3])
                .flat(addressParts[4])
                .build();
    }

    public DeliveryDto deliveryToDeliveryDto(Delivery delivery) {
        return DeliveryDto.builder()
                .deliveryId(String.valueOf(delivery.getId()))
                .fromAddress(addressToAdressDto(delivery.getFromAddress()))
                .toAddress(addressToAdressDto(delivery.getToAddress()))
                .orderId(delivery.getOrderId())
                .deliveryState(delivery.getState().name())
                .weigh(delivery.getWeigh())
                .volume(delivery.getVolume())
                .fragile(delivery.getFragile())
                .build();
    }

}
