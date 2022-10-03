package ru.practicum.ewm.models;

/**
 * Класс, описывающий способ сортировки событий
 *
 * @since 1.1
 */
public enum EventSortType {
    /**
     * Сортировка по дате начала события
     *
     * @since 1.1
     */
    EVENT_DATE,
    /**
     * Сортировка по количеству просмотров события
     *
     * @since 1.1
     */
    VIEWS,
    /**
     * Сортировка по рейтингу события
     *
     * @since 1.1
     */
    RATING
}
