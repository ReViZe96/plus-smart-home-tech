package ru.yandex.practicum.dto.request;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
public class ProductReturnRequest {

    private UUID orderId;
    @NotNull
    private Map<String, Integer> products;

}
