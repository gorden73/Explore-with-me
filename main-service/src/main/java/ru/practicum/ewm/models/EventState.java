package ru.practicum.ewm.models;

/**
 * Класс, описывающий состояния, в которых может находиться событие
 *
 * @since 1.0
 */
public enum EventState {
    /**
     * В ожидании модерации
     *
     * @since 1.0
     */
    PENDING,
    /**
     * Опубликовано
     *
     * @since 1.0
     */
    PUBLISHED,
    /**
     * Отменено пользователем
     *
     * @since 1.0
     */
    CANCELED,
    /**
     * Отклонено от публикации администратором
     *
     * @since 1.0
     */
    REJECT
}
