package ru.practicum.ewm.exceptions;

import java.util.List;

public class ConflictException extends MainException {
    public ConflictException(List<String> errors, String message, String reason) {
        super(errors, message, reason);
    }
}
