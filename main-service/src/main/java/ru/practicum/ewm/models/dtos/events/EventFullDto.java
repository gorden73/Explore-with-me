package ru.practicum.ewm.models.dtos.events;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.ewm.apis.admins.dtos.categories.CategoryDto;
import ru.practicum.ewm.apis.authorizedusers.dtos.users.UserShortDto;
import ru.practicum.ewm.models.EventState;

/**
 * Класс, описывающий Dto события с подробной информацией о нем
 *
 * @since 1.0
 */
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
public class EventFullDto {
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
    private String createdOn;
    /**
     * Полное описание события
     *
     * @since 1.0
     */
    private String description;
    /**
     * Дата и время, на которые намечено событие (в формате "yyyy-MM-dd HH:mm:ss")
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
     * Ограничение на количество участников. Значение 0 - означает отсутствие ограничения. По умолчанию 0.
     *
     * @since 1.0
     */
    private int participantLimit;
    /**
     * Дата и время публикации события
     *
     * @since 1.0
     */
    private String publishedOn;
    /**
     * Нужна ли пре-модерация заявок на участие(по умолчанию true)
     *
     * @since 1.0
     */
    private boolean requestModeration;
    /**
     * Статус события {@link EventState}
     *
     * @since 1.0
     */
    private String state;
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

    public EventFullDto(String annotation, CategoryDto category, int confirmedRequests, String createdOn,
                        String description, String eventDate, int id, UserShortDto initiator, boolean paid,
                        int participantLimit, boolean requestModeration, String state, String title, int views) {
        this.annotation = annotation;
        this.category = category;
        this.confirmedRequests = confirmedRequests;
        this.createdOn = createdOn;
        this.description = description;
        this.eventDate = eventDate;
        this.id = id;
        this.initiator = initiator;
        this.paid = paid;
        this.participantLimit = participantLimit;
        this.requestModeration = requestModeration;
        this.state = state;
        this.title = title;
        this.views = views;
    }
}
