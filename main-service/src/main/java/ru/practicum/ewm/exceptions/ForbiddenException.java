package ru.practicum.ewm.exceptions;

import java.util.List;

public class ForbiddenException extends MainException {

    public ForbiddenException(List<String> errors, String message, String reason) {
        super(errors, message, reason);
    }
}
