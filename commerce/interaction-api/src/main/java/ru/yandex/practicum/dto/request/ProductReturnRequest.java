package ru.yandex.practicum.dto.request;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Map;

@Data
@Builder
public class ProductReturnRequest {

    private String orderId;
    @NotNull
    private Map<String, Integer> products;

}
