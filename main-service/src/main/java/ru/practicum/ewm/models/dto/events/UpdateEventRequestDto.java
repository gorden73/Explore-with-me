package ru.practicum.ewm.models.dto.events;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateEventRequestDto extends EventDto {
    private Integer eventId;

    public UpdateEventRequestDto(String annotation, Integer category, String description, String eventDate,
                                 Boolean paid, Integer participantLimit, String title, Integer eventId) {
        super(annotation, category, description, eventDate, paid, participantLimit, title);
        this.eventId = eventId;
    }
}
