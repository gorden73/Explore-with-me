package ru.practicum.ewm.apis.authorizedusers.dtos.mappers;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.apis.admins.dtos.mappers.CategoryMapper;
import ru.practicum.ewm.apis.admins.dtos.mappers.UserMapper;
import ru.practicum.ewm.models.Event;
import ru.practicum.ewm.models.dtos.events.EventFullDto;
import ru.practicum.ewm.models.dtos.events.EventShortDto;
import ru.practicum.ewm.apis.authorizedusers.dtos.events.NewEventDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class EventMapper {
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static Event toEvent(NewEventDto dto) {
        return new Event(
                dto.getAnnotation(),
                dto.getDescription(),
                LocalDateTime.parse(dto.getEventDate(), FORMATTER),
                dto.getPaid(),
                dto.getParticipantLimit(),
                dto.getRequestModeration(),
                dto.getTitle());
    }

    public static EventShortDto toEventDto(Event event) {
        return new EventShortDto(
                event.getAnnotation(),
                CategoryMapper.toDto(event.getCategory()),
                event.getConfirmedRequests(),
                event.getEventDate().format(FORMATTER),
                event.getId(),
                UserMapper.toShortDto(event.getInitiator()),
                event.isPaid(),
                event.getTitle(),
                event.getViews());
    }

    public static Set<EventShortDto> toEventDtoCollection(Collection<Event> eventList) {
        return eventList.stream()
                .map(EventMapper::toEventDto)
                .collect(Collectors.toSet());
    }

    public static Collection<EventFullDto> toEventFullDtoCollection(Collection<Event> eventList) {
        return eventList.stream()
                .map(EventMapper::toEventFullDto)
                .collect(Collectors.toList());
    }

    public static EventFullDto toEventFullDto(Event event) {
        EventFullDto dto = new EventFullDto(
                event.getAnnotation(),
                CategoryMapper.toDto(event.getCategory()),
                event.getConfirmedRequests(),
                event.getCreatedOn().format(FORMATTER),
                event.getDescription(),
                event.getEventDate().format(FORMATTER),
                event.getId(),
                UserMapper.toShortDto(event.getInitiator()),
                event.isPaid(),
                event.getParticipantLimit(),
                event.isRequestModeration(),
                event.getState().toString(),
                event.getTitle(),
                event.getViews());
        if (event.getPublishedOn() == null) {
            dto.setPublishedOn(null);
        } else {
            dto.setPublishedOn(event.getPublishedOn().toString());
        }
        return dto;
    }
}
