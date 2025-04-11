package ru.yandex.practicum.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class DimensionDto {

    @NotNull
    @Min(1)
    private Double width;
    @NotNull
    @Min(1)
    private Double height;
    @NotNull
    @Min(1)
    private Double depth;

}
