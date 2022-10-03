package ru.practicum.ewm.models.dtos.events;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.ewm.controllers.apis.admins.dtos.categories.CategoryDto;
import ru.practicum.ewm.controllers.apis.authorizedusers.dtos.users.UserShortDto;

/**
 * Класс, описывающий Dto события с краткой информацией о нем
 *
 * @since 1.0
 */
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
public class EventShortDto {
    /**
     * Краткое описание события
     *
     * @since 1.0
     */
    private String annotation;
    /**
     * Dto категории события
     *
     * @since 1.0
     */
    private CategoryDto category;
    /**
     * Количество одобренных заявок на участие в данном событии
     *
     * @since 1.0
     */
    private int confirmedRequests;
    /**
     * Дата и время создания события
     *
     * @since 1.0
     */
    private String eventDate;
    /**
     * Идентификатор события
     *
     * @since 1.0
     */
    private int id;
    /**
     * Dto организатора события {@link UserShortDto}
     *
     * @since 1.0
     */
    private UserShortDto initiator;
    /**
     * Нужно ли оплачивать участие(по умолчанию false)
     *
     * @since 1.0
     */
    private boolean paid;
    /**
     * Заголовок события
     *
     * @since 1.0
     */
    private String title;
    /**
     * Количество просмотров события
     *
     * @since 1.0
     */
    private int views;
}
