package ru.yandex.practicum.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.model.Product;
import org.springframework.data.domain.Pageable;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {

    Page<Product> findAll(Pageable pageable);

    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.quantityState = ?2 WHERE p.productId = ?1")
    int updateProductQuantityState(String productId, String quantityState);

}
