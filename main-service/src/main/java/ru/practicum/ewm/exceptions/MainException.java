package ru.practicum.ewm.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Кастомный родительский класс, описывающий основные свойства исключений, присущие другим кастомным исключениям
 *
 * @since 1.0
 */
@Getter
@Setter
@AllArgsConstructor
public class MainException extends RuntimeException {
    /**
     * Список стектрейсов или сообщений о программных ошибках, возникших в процессе работы приложения
     *
     * @since 1.0
     */
    private List<String> errors;
    /**
     * Сообщение об ошибке
     *
     * @since 1.0
     */
    private String message;
    /**
     * Причина возникновения ошибки
     *
     * @since 1.0
     */
    private String reason;
}
