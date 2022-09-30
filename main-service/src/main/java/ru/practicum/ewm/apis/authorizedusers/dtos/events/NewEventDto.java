package ru.practicum.ewm.apis.authorizedusers.dtos.events;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

/**
 * Класс Dto, наследующий от {@link EventDto} и описывающий дополнительные свойства нового события, созданного
 * авторизованным пользователем
 *
 * @since 1.0
 */
@Getter
@Setter
public class NewEventDto extends EventDto {
    /**
     * Нужна ли пре-модерация заявок на участие(по умолчанию true)
     *
     * @since 1.0
     */
    private Boolean requestModeration;

    public NewEventDto(String annotation, Integer category, String description, String eventDate, Boolean paid,
                       Integer participantLimit, String title, Boolean requestModeration) {
        super(annotation, category, description, eventDate, Objects.requireNonNullElse(paid, false),
                Objects.requireNonNullElse(participantLimit, 0), title);
        this.requestModeration = Objects.requireNonNullElse(requestModeration, true);
    }
}
