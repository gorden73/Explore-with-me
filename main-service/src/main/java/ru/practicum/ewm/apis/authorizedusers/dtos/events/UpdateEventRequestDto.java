package ru.practicum.ewm.apis.authorizedusers.dtos.events;

import lombok.Getter;
import lombok.Setter;

/**
 * Класс Dto, наследующий от {@link EventDto} и описывающий дополнительные свойства для обновления события
 * авторизованным пользователем
 *
 * @since 1.0
 */
@Getter
@Setter
public class UpdateEventRequestDto extends EventDto {
    /**
     * Идентификатор события
     *
     * @since 1.0
     */
    private Integer eventId;

    public UpdateEventRequestDto(String annotation, Integer category, String description, String eventDate,
                                 Boolean paid, Integer participantLimit, String title, Integer eventId) {
        super(annotation, category, description, eventDate, paid, participantLimit, title);
        this.eventId = eventId;
    }
}
