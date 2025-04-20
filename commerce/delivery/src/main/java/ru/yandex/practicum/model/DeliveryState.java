package ru.yandex.practicum.model;

import java.util.Optional;

public enum DeliveryState {
    CREATED,
    IN_PROGRESS,
    DELIVERED,
    FAILED,
    CANCELLED;

    public static Optional<DeliveryState> from(String state) {
        switch (state.toUpperCase()) {
            case "CREATED":
                return Optional.of(CREATED);
            case "IN_PROGRESS":
                return Optional.of(IN_PROGRESS);
            case "DELIVERED":
                return Optional.of(DELIVERED);
            case "FAILED":
                return Optional.of(FAILED);
            case "CANCELLED":
                return Optional.of(CANCELLED);
            default:
                return Optional.empty();
        }
    }
}
