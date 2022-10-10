package ru.practicum.ewm.errors;

/**
 * Класс, описывающий HTTP коды ошибок
 *
 * @since 1.0
 */
public enum ErrorStatus {
    /**
     * Не выполнены условия для совершения операции
     *
     * @since 1.0
     */
    FORBIDDEN,
    /**
     * Объект не найден
     *
     * @since 1.0
     */
    NOT_FOUND,
    /**
     * Запрос приводит к нарушению целостности данных
     *
     * @since 1.0
     */
    CONFLICT,
    /**
     * Внутренняя ошибка сервера
     *
     * @since 1.0
     */
    INTERNAL_SERVER_ERROR,
    /**
     * Запрос составлен с ошибкой
     *
     * @since 1.0
     */
    BAD_REQUEST
}
