package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dto.AddressDto;
import ru.yandex.practicum.dto.BookedProductsDto;
import ru.yandex.practicum.dto.ShoppingCartDto;
import ru.yandex.practicum.dto.request.AddProductToWarehouseRequest;
import ru.yandex.practicum.dto.request.NewProductInWarehouseRequest;
import ru.yandex.practicum.mapper.WarehouseMapper;
import ru.yandex.practicum.repository.WarehouseRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {

    private static Logger logger = LoggerFactory.getLogger(WarehouseServiceImpl.class);

    private final WarehouseRepository warehouseRepository;
    private final WarehouseMapper warehouseMapper;


    //в случае, когда товар с данным описанием уже имеется на складе (400) - SpecifiedProductAlreadyInWarehouseException
    @Override
    public void addNewProduct(NewProductInWarehouseRequest newProductInWarehouse) {

    }

    //в случае, когда товар из корзины не находится в требуемом количестве на складе (400) - ProductInShoppingCartLowQuantityInWarehouse
    @Override
    public BookedProductsDto checkProductAmount(ShoppingCartDto shoppingCart) {
        return BookedProductsDto.builder().build();
    }

    //в случае, когда нет информации о товаре на складе (400) - NoSpecifiedProductInWarehouseException
    @Override
    public void reviseProduct(AddProductToWarehouseRequest addProductToWarehouse) {

    }

    @Override
    public AddressDto getWarehouseAddress() {
        return AddressDto.builder().build();
    }

}
