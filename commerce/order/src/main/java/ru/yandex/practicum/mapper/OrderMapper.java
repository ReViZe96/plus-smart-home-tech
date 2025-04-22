package ru.yandex.practicum.mapper;

import org.mapstruct.Mapper;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.model.Order;

@Mapper
public interface OrderMapper {

    OrderDto orderToOrderDto(Order order);

    Order orderDtoToOrder(OrderDto orderDto);

}
