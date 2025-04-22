package ru.yandex.practicum.model;

import java.util.Optional;

public enum PaymentStatus {
    PENDING,
    SUCCESS,
    FAILED;

    public static Optional<PaymentStatus> from(String state) {
        switch (state.toUpperCase()) {
            case "PENDING":
                return Optional.of(PENDING);
            case "SUCCESS":
                return Optional.of(SUCCESS);
            case "FAILED":
                return Optional.of(FAILED);
            default:
                return Optional.empty();
        }
    }

}
