package ru.practicum.ewm.exceptions;

import java.util.List;

/**
 * Кастомный класс, описывающий исключение, вызванное запросом, составленным с ошибкой
 *
 * @see MainException
 * @since 1.0
 */
public class BadRequestException extends MainException {
    public BadRequestException(List<String> errors, String message, String reason) {
        super(errors, message, reason);
    }
}
