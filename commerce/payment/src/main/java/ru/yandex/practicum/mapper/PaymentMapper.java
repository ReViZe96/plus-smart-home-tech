package ru.yandex.practicum.mapper;

import org.mapstruct.Mapper;
import ru.yandex.practicum.dto.PaymentDto;
import ru.yandex.practicum.model.Payment;

@Mapper
public interface PaymentMapper {

    PaymentDto paymentToPaymentDto(Payment payment);

}
