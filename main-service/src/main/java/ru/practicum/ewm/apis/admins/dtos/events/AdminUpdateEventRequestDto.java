package ru.practicum.ewm.apis.admins.dtos.events;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.ewm.apis.authorizedusers.dtos.events.EventDto;

@Getter
@Setter
public class AdminUpdateEventRequestDto extends EventDto {
    private Boolean requestModeration;

    public AdminUpdateEventRequestDto(String annotation, Integer category, String description, String eventDate,
                                      Boolean paid, Integer participantLimit, String title, Boolean requestModeration) {
        super(annotation, category, description, eventDate, paid, participantLimit, title);
        this.requestModeration = requestModeration;
    }
}
