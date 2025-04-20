package ru.yandex.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.model.Delivery;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, String> {



}
