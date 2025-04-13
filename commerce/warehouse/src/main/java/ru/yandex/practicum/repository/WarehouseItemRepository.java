package ru.yandex.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.model.WarehouseItem;

@Repository
public interface WarehouseItemRepository extends JpaRepository<WarehouseItem, String> {

    @Modifying
    @Transactional
    @Query("UPDATE WarehouseItem i SET i.quantity = ?2 WHERE i.id = ?1")
    int updateWarehouseItemQuantity(String id, Long quantity);

}
