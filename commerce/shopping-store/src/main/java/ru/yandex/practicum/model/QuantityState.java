package ru.yandex.practicum.model;

import java.util.Optional;

public enum QuantityState {
    ENDED,
    FEW,
    ENOUGH,
    MANY;

    public static Optional<QuantityState> from(String quantityState) {
        switch (quantityState.toUpperCase()) {
            case "ENDED":
                return Optional.of(ENDED);
            case "FEW":
                return Optional.of(FEW);
            case "ENOUGH":
                return Optional.of(ENOUGH);
            case "MANY":
                return Optional.of(MANY);
            default:
                return Optional.empty();
        }
    }
}
