package ru.practicum.ewm.models.dto.events;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.util.Objects;

@Getter
@Setter
public class NewEventDto extends EventDto {
    private Boolean requestModeration;

    public NewEventDto(String annotation, Integer category, String description, String eventDate, Boolean paid,
                       Integer participantLimit, String title, Boolean requestModeration) {
        super(annotation, category, description, eventDate, Objects.requireNonNullElse(paid, false),
                Objects.requireNonNullElse(participantLimit, 0), title);
        this.requestModeration = Objects.requireNonNullElse(requestModeration, true);
    }
}
