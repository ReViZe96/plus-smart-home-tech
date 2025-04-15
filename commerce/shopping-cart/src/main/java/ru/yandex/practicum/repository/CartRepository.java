package ru.yandex.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.model.Cart;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, String> {

    Optional<Cart> findByUsername(String username);

    @Modifying
    @Query("UPDATE Cart c SET c.active = ?2 WHERE c.shoppingCartId = ?1")
    int updateCartActive(String shoppingCartId, Boolean active);


}
