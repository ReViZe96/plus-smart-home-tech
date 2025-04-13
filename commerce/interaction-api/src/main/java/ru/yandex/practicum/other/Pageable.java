package ru.yandex.practicum.other;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;

@Data
@Builder
public class Pageable {

    @Min(0)
    private Integer page;
    @Min(1)
    private Integer size;
    private String sort;

}
