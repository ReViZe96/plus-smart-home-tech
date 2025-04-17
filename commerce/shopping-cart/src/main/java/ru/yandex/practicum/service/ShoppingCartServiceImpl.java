package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.clients.ShoppingStoreClient;
import ru.yandex.practicum.clients.WarehouseClient;
import ru.yandex.practicum.dto.ProductDto;
import ru.yandex.practicum.dto.ShoppingCartDto;
import ru.yandex.practicum.dto.request.ChangeProductQuantityRequest;
import ru.yandex.practicum.exception.NoProductsInShoppingCartException;
import ru.yandex.practicum.exception.NotAuthorizedUserException;
import ru.yandex.practicum.mapper.CartMapper;
import ru.yandex.practicum.model.Cart;
import ru.yandex.practicum.repository.CartRepository;

import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private static Logger logger = LoggerFactory.getLogger(ShoppingCartServiceImpl.class);

    private final CartRepository cartRepository;
    private final CartMapper cartMapper;
    private final ShoppingStoreClient shoppingStoreClient;
    private final WarehouseClient warehouseClient;


    @Override
    public ShoppingCartDto getCart(String username) {
        checkUsername(username);
        return cartMapper.cartToShoppingCartDto(getUserCartOrCreate(username));
    }

    @Override
    public ShoppingCartDto addProductsToCart(String username, Map<String, Integer> addingProducts) {
        checkUsername(username);
        Cart usersCart = getUserCartOrCreate(username);

        if (!checkActive(usersCart)) {
            log.warn("Возвращаю текущее состояние корзины {} пользователя {} без изменений",
                    usersCart.getShoppingCartId(), username);
            return cartMapper.cartToShoppingCartDto(usersCart);
        }

        Map<String, Integer> productsInCart = usersCart.getProducts();
        productsInCart.putAll(addingProducts);
        usersCart.setProducts(productsInCart);
        ShoppingCartDto updatedUsersCart = cartMapper.cartToShoppingCartDto(usersCart);

        log.debug("Проверка наличия добавляемого в корзину товара в нужном количестве на складе - вызов внешнего сервиса");
        warehouseClient.checkProductAmountInWarehouse(updatedUsersCart);

        cartRepository.save(cartMapper.shoppingCartDtoToCart(updatedUsersCart));
        return updatedUsersCart;
    }

    @Override
    @Transactional
    public void deactivateCart(String username) {
        checkUsername(username);
        Optional<Cart> deactivatingCart = cartRepository.findByUsername(username);
        if (deactivatingCart.isPresent() && deactivatingCart.get().getActive()) {
            cartRepository.updateCartActive(deactivatingCart.get().getShoppingCartId(), false);
        } else {
            log.warn("На деактивацию передана уже деактивированная корзина: {}", deactivatingCart.get().getShoppingCartId());
        }
    }

    @Override
    public ShoppingCartDto removeProductFromCart(String username, Map<String, Integer> removingProducts) {
        checkUsername(username);
        Optional<Cart> updatingCart = cartRepository.findByUsername(username);
        if (updatingCart.isPresent() && !updatingCart.get().getProducts().isEmpty()) {
            if (checkActive(updatingCart.get())) {
                Map<String, Integer> productsInCart = updatingCart.get().getProducts();
                log.debug("Старт удаления товаров из корзины: {}", updatingCart.get().getShoppingCartId());
                productsInCart = compareAndRemove(removingProducts, productsInCart);
                Cart updatedCart = updatingCart.get();
                updatedCart.setProducts(productsInCart);
                return cartMapper.cartToShoppingCartDto(cartRepository.save(updatedCart));
            } else {
                log.warn("Возвращаю текущее состояние корзины {} пользователя {} без изменений",
                        updatingCart.get().getShoppingCartId(), username);
                return cartMapper.cartToShoppingCartDto(updatingCart.get());
            }
        } else {
            throw new NoProductsInShoppingCartException("Попытка удалить товары из несуществующей, либо пустой корзины");

        }
    }

    @Override
    public ProductDto setProductAmountInCart(String username, ChangeProductQuantityRequest changeProductQuantity) {
        checkUsername(username);
        Optional<Cart> updatingCart = cartRepository.findByUsername(username);
        if (updatingCart.isPresent()) {
            if (checkActive(updatingCart.get())) {
                log.debug("Cтарт изменения информации о количестве товара {} в корзине {}",
                        changeProductQuantity.getProductId(), updatingCart.get().getShoppingCartId());
                setProductAmount(updatingCart.get(), changeProductQuantity);
            } else {
                log.warn("Невозможно изменить количество товара {} в корзине {}",
                        changeProductQuantity.getProductId(), updatingCart.get().getShoppingCartId());
            }
            log.debug("Получение информации о продукте {}, из внешнего сервиса",
                    changeProductQuantity.getProductId());
            return shoppingStoreClient.getProductById(changeProductQuantity.getProductId());
        } else {
            throw new NoProductsInShoppingCartException("Попытка изменить количество товара в несуществующей корзине");
        }
    }


    private void checkUsername(String username) {
        if (username.isBlank()) {
            throw new NotAuthorizedUserException("Пользователя с именем: " + username + " не существует");
        }
    }

    private Cart getUserCartOrCreate(String username) {
        Optional<Cart> cart = cartRepository.findByUsername(username);
        if (cart.isPresent()) {
            return cart.get();
        }
        Cart newCart = new Cart();
        newCart.setUsername(username);
        return cartRepository.save(newCart);
    }

    private boolean checkActive(Cart cart) {
        if (!cart.getActive()) {
            log.warn("Корзина: {} неактивна.", cart.getShoppingCartId());
            return false;
        }
        return true;
    }

    private Map<String, Integer> compareAndRemove(Map<String, Integer> removingProducts, Map<String, Integer> productsInCart) {
        for (String product : removingProducts.keySet()) {
            if (productsInCart.containsKey(product)
                    && productsInCart.get(product) > removingProducts.get(product)) {
                productsInCart.put(product, productsInCart.get(product) - removingProducts.get(product));
            } else {
                throw new NoProductsInShoppingCartException("Искомый товар: " + product + " отсутствует в корзине" +
                        " в удаляемом количестве");
            }
        }
        return productsInCart;
    }

    private void setProductAmount(Cart updatingCart, ChangeProductQuantityRequest changeProductQuantity) {
        Map<String, Integer> productsInCart = updatingCart.getProducts();
        productsInCart.put(changeProductQuantity.getProductId(), Math.toIntExact(changeProductQuantity.getNewQuantity()));
        updatingCart.setProducts(productsInCart);

        ShoppingCartDto updatingCartDto = cartMapper.cartToShoppingCartDto(updatingCart);
        log.debug("Проверка наличия добавляемого в корзину товара в нужном количестве на складе - вызов внешнего сервиса");
        warehouseClient.checkProductAmountInWarehouse(updatingCartDto);
        cartRepository.save(updatingCart);
    }

}
