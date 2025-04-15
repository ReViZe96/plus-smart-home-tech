package ru.yandex.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {

    @Modifying
    @Query("UPDATE Product p SET p.quantityState = ?2 WHERE p.productId = ?1")
    int updateProductQuantityState(String productId, String quantityState);

}
