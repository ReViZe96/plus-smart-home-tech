package ru.yandex.practicum.model;

import java.util.Optional;

public enum ProductCategory {
    LIGHTING,
    CONTROL,
    SENSORS;

    public static Optional<ProductCategory> from(String category) {
        switch (category.toUpperCase()) {
            case "LIGHTING":
                return Optional.of(LIGHTING);
            case "CONTROL":
                return Optional.of(CONTROL);
            case "SENSORS":
                return Optional.of(SENSORS);
            default:
                return Optional.empty();
        }
    }
}
