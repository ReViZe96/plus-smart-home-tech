package ru.yandex.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.practicum.dto.request.NewProductInWarehouseRequest;
import ru.yandex.practicum.model.WarehouseItem;

@Mapper
public interface WarehouseMapper {

    @Mapping(target = "quantity", ignore = true)
    @Mapping(target = "width", source = "dimension.width")
    @Mapping(target = "height", source = "dimension.height")
    @Mapping(target = "depth", source = "dimension.depth")
    WarehouseItem newProductRequestToWarehouseItem(NewProductInWarehouseRequest request);

}
