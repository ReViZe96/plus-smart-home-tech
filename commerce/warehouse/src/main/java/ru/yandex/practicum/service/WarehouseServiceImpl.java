package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.dto.AddressDto;
import ru.yandex.practicum.dto.BookedProductsDto;
import ru.yandex.practicum.dto.ShoppingCartDto;
import ru.yandex.practicum.dto.request.AddProductToWarehouseRequest;
import ru.yandex.practicum.dto.request.NewProductInWarehouseRequest;
import ru.yandex.practicum.exception.NoSpecifiedProductInWarehouseException;
import ru.yandex.practicum.exception.ProductInShoppingCartLowQuantityInWarehouse;
import ru.yandex.practicum.exception.SpecifiedProductAlreadyInWarehouseException;
import ru.yandex.practicum.mapper.WarehouseMapper;
import ru.yandex.practicum.model.Warehouse;
import ru.yandex.practicum.model.WarehouseItem;
import ru.yandex.practicum.repository.WarehouseItemRepository;
import ru.yandex.practicum.repository.WarehouseRepository;

import java.security.SecureRandom;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {

    private static Logger logger = LoggerFactory.getLogger(WarehouseServiceImpl.class);

    //заглушка
    private Warehouse warehouse = addNewWarehouse(initAddressDto());

    private final WarehouseItemRepository warehouseItemRepository;
    private final WarehouseRepository warehouseRepository;
    private final WarehouseMapper warehouseMapper;


    @Override
    public void createNewItem(NewProductInWarehouseRequest newProductInWarehouse) {
        if (warehouseItemRepository.findById(newProductInWarehouse.getProductId()).isPresent()) {
            throw new SpecifiedProductAlreadyInWarehouseException("Добавляемая товарная позиция: " +
                    newProductInWarehouse.getProductId() +
                    " уже имеется на складе");
        }
        log.debug("Старт сохранения информации о новой товарной позиции {} на складе", newProductInWarehouse.getProductId());
        WarehouseItem newItem = warehouseMapper.newProductRequestToWarehouseItem(newProductInWarehouse);
        newItem.setQuantity(0);
        warehouseItemRepository.save(newItem);
    }

    @Override
    public BookedProductsDto checkProductAmount(ShoppingCartDto shoppingCart) {
        Map<String, Integer> productsInCart = shoppingCart.getProducts();
        Map<WarehouseItem, Integer> productsInWarehouse = new HashMap<>();
        for (String productId : productsInCart.keySet()) {
            Optional<WarehouseItem> item = warehouseItemRepository.findById(productId);
            if (item.isEmpty()) {
                throw new NoSpecifiedProductInWarehouseException("Товар из корзины: " + productId +
                        " отсутствует на складе");
            }
            if (productsInCart.get(productId) > item.get().getQuantity()) {
                throw new ProductInShoppingCartLowQuantityInWarehouse("Недостаточное количество товара " +
                        productId + " на складе");
            }
            log.debug("Товары из корзины присутствуют на складе в требуемом количестве");
            productsInWarehouse.put(item.get(), productsInCart.get(productId));
        }
        return calculateDelivery(productsInWarehouse);
    }

    @Override
    @Transactional
    public void addProduct(AddProductToWarehouseRequest addProductToWarehouse) {
        Optional<WarehouseItem> updatingItem = warehouseItemRepository.findById(addProductToWarehouse.getProductId());
        if (updatingItem.isEmpty()) {
            throw new NoSpecifiedProductInWarehouseException("Невозможно принять товар: " +
                    addProductToWarehouse.getProductId() +
                    " т.к. информация по данной товарной позиции отсутствует на складе");
        }
        Long quantity = updatingItem.get().getQuantity() + addProductToWarehouse.getQuantity();
        int result = warehouseItemRepository.updateWarehouseItemQuantity(addProductToWarehouse.getProductId(), quantity);
        if (result > 0) {
            log.debug("Принято {} единиц товара: {}. ", addProductToWarehouse.getQuantity(), addProductToWarehouse.getProductId());
        }
    }

    @Override
    public AddressDto getWarehouseAddress() {
        //заглушка
        Optional<Warehouse> foundedWarehouse = warehouseRepository.findById("1");
        if (foundedWarehouse.isEmpty()) {
            throw new RuntimeException("Склада с указанным id не существует");
        }
        return AddressDto.builder()
                .city(foundedWarehouse.get().getCity())
                .street(foundedWarehouse.get().getStreet())
                .house(foundedWarehouse.get().getHouse())
                .country(foundedWarehouse.get().getCountry())
                .flat(foundedWarehouse.get().getFlat())
                .build();
    }


    private BookedProductsDto calculateDelivery(Map<WarehouseItem, Integer> productsInWarehouse) {
        Double commonWeight = 0.0;
        Double commonVolume = 0.0;
        log.debug("Старт вычисления параметров доставки товаров из корзины");
        for (WarehouseItem item : productsInWarehouse.keySet()) {
            commonWeight += item.getWeight() * productsInWarehouse.get(item);
            Double currentVolume = item.getDepth() * item.getHeight() * item.getWidth() * productsInWarehouse.get(item);
            commonVolume += currentVolume;
        }
        Boolean fragile = isDeliveryFragile(productsInWarehouse.keySet());

        return BookedProductsDto.builder()
                .deliveryWeight(commonWeight)
                .deliveryVolume(commonVolume)
                .fragile(fragile)
                .build();
    }

    private Boolean isDeliveryFragile(Set<WarehouseItem> productsInWarehouse) {
        return productsInWarehouse.stream().anyMatch(WarehouseItem::getFragile);
    }

    //переделается в публичный вызываемый извне метод сервиса для создания склада
    private Warehouse addNewWarehouse(AddressDto address) {
        Warehouse newWarehouse = new Warehouse();
        newWarehouse.setId("1");
        newWarehouse.setCity(address.getCity());
        newWarehouse.setStreet(address.getStreet());
        newWarehouse.setHouse(address.getHouse());
        newWarehouse.setCountry(address.getCountry());
        newWarehouse.setFlat(address.getFlat());
        warehouseRepository.save(newWarehouse);
        return warehouse;
    }

    //заглушка
    private AddressDto initAddressDto() {
        final String[] addresses = new String[]{"ADDRESS_1", "ADDRESS_2"};
        final String address = addresses[Random.from(new SecureRandom()).nextInt(0, 1)];
        return AddressDto.builder()
                .city(address)
                .street(address)
                .house(address)
                .country(address)
                .flat(address)
                .build();
    }

}
