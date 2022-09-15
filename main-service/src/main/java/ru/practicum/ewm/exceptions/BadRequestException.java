package ru.practicum.ewm.exceptions;

import java.util.List;

public class BadRequestException extends MainException {
    public BadRequestException(List<String> errors, String message, String reason) {
        super(errors, message, reason);
    }
}
