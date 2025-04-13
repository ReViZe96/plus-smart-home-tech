package ru.yandex.practicum.mapper;

import ru.yandex.practicum.dto.request.NewProductInWarehouseRequest;
import ru.yandex.practicum.model.WarehouseItem;

public class WarehouseMapper {

    public WarehouseItem newProductRequestToProductItem(NewProductInWarehouseRequest request) {
        WarehouseItem addingWarehouseItem = new WarehouseItem();
        addingWarehouseItem.setId(request.getProductId());
        addingWarehouseItem.setQuantity(0);
        addingWarehouseItem.setFragile(request.getFragile());
        addingWarehouseItem.setWeight(request.getWeight());
        addingWarehouseItem.setWidth(request.getDimension().getWidth());
        addingWarehouseItem.setHeight(request.getDimension().getHeight());
        addingWarehouseItem.setDepth(request.getDimension().getDepth());
        return addingWarehouseItem;
    }

}
