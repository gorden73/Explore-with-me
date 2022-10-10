package ru.practicum.ewm.exceptions;

import java.util.List;

/**
 * Кастомный класс, описывающий исключение, вызванное запросом, приводящим к нарушению целостности данных
 *
 * @see MainException
 * @since 1.0
 */
public class ConflictException extends MainException {
    public ConflictException(List<String> errors, String message, String reason) {
        super(errors, message, reason);
    }
}
