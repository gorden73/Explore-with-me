package ru.practicum.ewm.exceptions;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class NotFoundException extends MainException {
    public NotFoundException(List<String> errors, String message, String reason) {
        super(errors, message, reason);
    }
}
