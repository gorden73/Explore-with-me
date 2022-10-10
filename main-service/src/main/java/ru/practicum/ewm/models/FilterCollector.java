package ru.practicum.ewm.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Класс, описывающий параметры фильтрации событий
 *
 * @since 1.1
 */
@Getter
@Setter
@NoArgsConstructor
public class FilterCollector {
    /**
     * Текст для поиска в содержимом аннотации и подробном описании события
     *
     * @since 1.1
     */
    private String text;
    /**
     * Список идентификаторов категорий, в которых будет вестись поиск
     *
     * @since 1.1
     */
    private Integer[] categories;
    /**
     * Платные/бесплатные события
     *
     * @since 1.1
     */
    private Boolean paid;
    /**
     * Дата и время, не раньше которых должно произойти событие
     *
     * @since 1.1
     */
    private String rangeStart;
    /**
     * Дата и время, не позже которых должно произойти событие
     *
     * @since 1.1
     */
    private String rangeEnd;
    /**
     * Поиск только доступных/недоступных для участия событий (по умолчанию false)
     *
     * @since 1.1
     */
    private boolean onlyAvailable;
    /**
     * Вариант сортировки: по дате события (EVENT_DATE), по количеству просмотров (VIEWS) или по рейтингу (RATING)
     *
     * @since 1.1
     */
    private String sort;
    /**
     * количество событий, которые нужно пропустить для формирования текущего набора (по умолчанию 0)
     *
     * @since 1.1
     */
    private int from;
    /**
     * Количество событий в наборе (по умолчанию 10)
     *
     * @since 1.1
     */
    private int size;
}
