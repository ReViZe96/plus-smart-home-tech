package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.clients.OrderClient;
import ru.yandex.practicum.dto.*;
import ru.yandex.practicum.dto.request.AddProductToWarehouseRequest;
import ru.yandex.practicum.dto.request.AssemblyProductsForOrderRequest;
import ru.yandex.practicum.dto.request.NewProductInWarehouseRequest;
import ru.yandex.practicum.dto.request.ShippedToDeliveryRequest;
import ru.yandex.practicum.exception.NoSpecifiedProductInWarehouseException;
import ru.yandex.practicum.exception.ProductInShoppingCartLowQuantityInWarehouse;
import ru.yandex.practicum.exception.SpecifiedProductAlreadyInWarehouseException;
import ru.yandex.practicum.mapper.WarehouseMapper;
import ru.yandex.practicum.model.Warehouse;
import ru.yandex.practicum.model.WarehouseItem;
import ru.yandex.practicum.repository.WarehouseItemRepository;
import ru.yandex.practicum.repository.WarehouseRepository;

import javax.validation.ValidationException;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {

    public static final String ADDRESS_1 = "ADDRESS_1";
    public static final String ADDRESS_2 = "ADDRESS_2";

    private final WarehouseItemRepository warehouseItemRepository;
    private final WarehouseRepository warehouseRepository;
    private final WarehouseMapper warehouseMapper;
    private final OrderClient orderClient;


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
    public DeliveryDto shippedProductsToDelivery(ShippedToDeliveryRequest shippedToDelivery) {
        log.debug("Старт поиска заказа {}, передаваемого в доставку ", shippedToDelivery.getOrderId());
        OrderDto shippedOrder = orderClient.getOrderById(shippedToDelivery.getOrderId());
        log.debug("Старт сбора товаров для заказа {}", shippedOrder.getOrderId());
        AssemblyProductsForOrderRequest assemblyProductsForOrder = AssemblyProductsForOrderRequest
                .builder()
                .orderId(shippedOrder.getOrderId())
                .products(shippedOrder.getProducts())
                .build();
        BookedProductsDto bookedProducts = assemblyProductsForShipment(assemblyProductsForOrder);
        log.debug("Заказ {} для передачи в доставку собран и перепроверен", shippedOrder.getOrderId());
        return DeliveryDto.builder()
                .deliveryId(String.valueOf(shippedToDelivery.getDeliveryId()))
                .orderId(shippedToDelivery.getOrderId())
                .volume(bookedProducts.getDeliveryVolume())
                .weigh(bookedProducts.getDeliveryWeight())
                .fragile(bookedProducts.getFragile())
                .build();
    }

    @Override
    public Boolean returnProductsToWarehouse(Map<String, Integer> returnedProducts) {
        int amountOfReturnedProducts = 0;
        for (String productId : returnedProducts.keySet()) {
            Optional<WarehouseItem> item = warehouseItemRepository.findById(productId);
            if (item.isEmpty()) {
                throw new NoSpecifiedProductInWarehouseException("Возвращаемый товар: " + productId +
                        " отсутствует на складе");
            }
            WarehouseItem returningItem = item.get();
            Integer returningAmount = returnedProducts.get(productId);
            returningItem.setQuantity(returningItem.getQuantity() + returningAmount);
            warehouseItemRepository.save(returningItem);
            log.debug("Возвращено {} штук позиции {} на склад", returningAmount, returningItem.getId());
            amountOfReturnedProducts++;
        }
        if (amountOfReturnedProducts == returnedProducts.size()) {
            log.debug("Все позиции из запроса на возврат успешно возвращены на склад");
            return true;
        } else {
            return false;
        }
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
    public BookedProductsDto assemblyProductsForShipment(AssemblyProductsForOrderRequest assemblyProductsForOrder) {
        Map<String, Integer> assemblingProducts = assemblyProductsForOrder.getProducts();
        Map<WarehouseItem, Integer> assembledProducts = new HashMap<>();
        BookedProductsDto bookedProducts = null;
        for (String productId : assemblingProducts.keySet()) {
            Optional<WarehouseItem> item = warehouseItemRepository.findById(productId);
            if (item.isEmpty()) {
                throw new NoSpecifiedProductInWarehouseException("Товар " + productId + " из заказа: " +
                        assemblyProductsForOrder.getOrderId() + " отсутствует на складе");
            }
            if (assemblingProducts.get(productId) > item.get().getQuantity()) {
                throw new ProductInShoppingCartLowQuantityInWarehouse("Недостаточное количество товара " +
                        productId + " на складе");
            }
            log.debug("Товары из заказа {} присутствуют на складе в требуемом количестве",
                    assemblyProductsForOrder.getOrderId());
            WarehouseItem assemblingItem = item.get();
            Integer assemblingAmount = assemblingProducts.get(productId);

            assembledProducts.put(assemblingItem, assemblingAmount);

            assemblingItem.setQuantity(assemblingItem.getQuantity() - assemblingAmount);
            warehouseItemRepository.save(assemblingItem);
            bookedProducts = calculateDelivery(assembledProducts);
            log.debug("Старт изменения статуса заказа {}, собранного на складе - вызов внешнего сервиса",
                    assemblyProductsForOrder.getOrderId());
            orderClient.assemblyOrder(assemblyProductsForOrder.getOrderId());
        }
        return bookedProducts;

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
    public AddressDto getWarehouseById(String warehouseId) {
        Optional<Warehouse> warehouse = warehouseRepository.findById(UUID.fromString(warehouseId));
        if (warehouse.isEmpty()) {
            throw new ValidationException("Склад с id = " + warehouseId + " не найден в системе");
        }
        Warehouse foundedWarehouse = warehouse.get();
        return AddressDto.builder()
                .city(foundedWarehouse.getCity())
                .street(foundedWarehouse.getStreet())
                .house(foundedWarehouse.getHouse())
                .country(foundedWarehouse.getCountry())
                .flat(foundedWarehouse.getFlat())
                .build();
    }

    @Override
    public AddressDto addWarehouse(AddressDto newAddress) {
        checkAddress(newAddress);
        Warehouse newWarehouse = new Warehouse();
        newWarehouse.setCity(newAddress.getCity());
        newWarehouse.setStreet(newAddress.getStreet());
        newWarehouse.setHouse(newAddress.getHouse());
        newWarehouse.setCountry(newAddress.getCountry());
        newWarehouse.setFlat(newAddress.getFlat());
        warehouseRepository.save(newWarehouse);
        return newAddress;
    }


    private BookedProductsDto calculateDelivery(Map<WarehouseItem, Integer> productsInWarehouse) {
        Double commonWeight = 0.0;
        Double commonVolume = 0.0;
        log.debug("Старт вычисления параметров доставки товаров");
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

    private void checkAddress(AddressDto address) {
        if (address == null || address.getCity() == null || address.getStreet() == null || address.getHouse() == null
                || address.getCountry() == null || address.getFlat() == null) {
            throw new ValidationException("Переда некорректный адрес для нового склада.");
        }
        Optional<Warehouse> warehouse = warehouseRepository.findByCityAndStreetAndHouseAndCountryAndFlat(address.getCity(),
                address.getStreet(), address.getHouse(), address.getCountry(), address.getFlat());
        if (warehouse.isPresent()) {
            throw new ValidationException("Склад, расположенный по данному адресу, уже зарегистрирован в системе");
        }
    }

}
