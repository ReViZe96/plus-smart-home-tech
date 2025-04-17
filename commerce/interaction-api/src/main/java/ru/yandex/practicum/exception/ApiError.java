package ru.yandex.practicum.exception;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ApiError {
    final List<String> errors;
    final String status;
    final String reason;
    final String message;
    final LocalDateTime timestamp;
}
