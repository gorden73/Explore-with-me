package ru.practicum.ewm.exceptions;

import java.util.List;

/**
 * Кастомный класс, описывающий исключение, вызванное запросом, при выполнении которого не выполнены условия для
 * совершения операции
 *
 * @see MainException
 * @since 1.0
 */
public class ForbiddenException extends MainException {
    public ForbiddenException(List<String> errors, String message, String reason) {
        super(errors, message, reason);
    }
}
