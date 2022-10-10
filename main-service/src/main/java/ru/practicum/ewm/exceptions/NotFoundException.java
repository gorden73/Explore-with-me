package ru.practicum.ewm.exceptions;

import java.util.List;

/**
 * Кастомный класс, описывающий исключение, вызванное отсутствием объекта, требуемого запросом
 *
 * @see MainException
 * @since 1.0
 */
public class NotFoundException extends MainException {
    public NotFoundException(List<String> errors, String message, String reason) {
        super(errors, message, reason);
    }
}
