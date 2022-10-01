package ru.practicum.stat.models;

import lombok.Getter;
import lombok.Setter;

/**
 * Класс, описывающий сущность статистики просмотров эндпоинта
 *
 * @since 1.0
 */
@Getter
@Setter
public class ViewStats {
    /**
     * Идентификатор сущности
     *
     * @since 1.0
     */
    private int id;

    /**
     * Идентификатор сервиса, для которого была записана информация
     *
     * @since 1.0
     */
    private String app;

    /**
     * URI, для которого был осуществлен запрос
     *
     * @since 1.0
     */
    private String uri;

    /**
     * Количество просмотров эндпоинта
     *
     * @since 1.0
     */
    private Integer hits;

    public ViewStats(String app, String uri, Integer hits) {
        this.app = app;
        this.uri = uri;
        this.hits = hits;
    }
}
