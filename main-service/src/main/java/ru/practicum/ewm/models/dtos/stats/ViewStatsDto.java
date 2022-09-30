package ru.practicum.ewm.models.dtos.stats;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Класс, описывающий Dto сущности статистики просмотров эндпоинта
 *
 * @since 1.0
 */
@Getter
@Setter
@AllArgsConstructor
public class ViewStatsDto {
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
}
