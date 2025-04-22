package ru.yandex.practicum.model;

import java.util.Optional;

public enum OrderState {
    NEW,
    PRODUCT_RETURNED,
    ON_PAYMENT,
    PAID,
    PAYMENT_FAILED,
    ASSEMBLED,
    ASSEMBLY_FAILED,
    ON_DELIVERY,
    DELIVERED,
    DELIVERY_FAILED,
    COMPLETED,
    DONE,
    CANCELED;

    public static Optional<OrderState> from(String state) {
        switch (state.toUpperCase()) {
            case "NEW":
                return Optional.of(NEW);
            case "PRODUCT_RETURNED":
                return Optional.of(PRODUCT_RETURNED);
            case "ON_PAYMENT":
                return Optional.of(ON_PAYMENT);
            case "PAID":
                return Optional.of(PAID);
            case "PAYMENT_FAILED":
                return Optional.of(PAYMENT_FAILED);
            case "ASSEMBLED":
                return Optional.of(ASSEMBLED);
            case "ASSEMBLY_FAILED":
                return Optional.of(ASSEMBLY_FAILED);
            case "ON_DELIVERY":
                return Optional.of(ON_DELIVERY);
            case "DELIVERED":
                return Optional.of(DELIVERED);
            case "DELIVERY_FAILED":
                return Optional.of(DELIVERY_FAILED);
            case "COMPLETED":
                return Optional.of(COMPLETED);
            case "DONE":
                return Optional.of(DONE);
            case "CANCELED":
                return Optional.of(CANCELED);
            default:
                return Optional.empty();
        }
    }
}
