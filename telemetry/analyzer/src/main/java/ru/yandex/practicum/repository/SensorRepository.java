package ru.yandex.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.model.Sensor;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface SensorRepository extends JpaRepository<Sensor, String> {

    Optional<Sensor> findByIdInAndHubId(String id, String hubId);

    Optional<Sensor> findByIdAndHubId(String id, String hubId);

}