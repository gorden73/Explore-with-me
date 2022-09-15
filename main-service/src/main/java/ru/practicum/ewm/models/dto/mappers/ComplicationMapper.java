package ru.practicum.ewm.models.dto.mappers;

import ru.practicum.ewm.models.Complication;
import ru.practicum.ewm.models.Event;
import ru.practicum.ewm.models.dto.complications.ComplicationDto;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.models.dto.complications.NewComplicationDto;

import java.util.Collection;
import java.util.stream.Collectors;

@Component
public class ComplicationMapper {
    public static ComplicationDto toDto(Complication complication) {
        return new ComplicationDto(
                complication.getEvents().stream().map(Event::getId).collect(Collectors.toSet()),
                complication.getId(),
                complication.isPinned(),
                complication.getTitle());
    }

    public static Collection<ComplicationDto> toDtoCollection(Collection<Complication> complications) {
        return complications.stream()
                .map(ComplicationMapper::toDto)
                .collect(Collectors.toList());
    }

    public static Complication toComplication(NewComplicationDto dto) {
        return new Complication(
                dto.isPinned(),
                dto.getTitle()
        );
    }
}
