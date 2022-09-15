package ru.practicum.ewm.models.dto.mappers;

import ru.practicum.ewm.models.Event;
import ru.practicum.ewm.models.dto.events.EventFullDto;
import ru.practicum.ewm.models.dto.events.EventShortDto;
import ru.practicum.ewm.models.dto.events.NewEventDto;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.stream.Collectors;

@Component
public class EventMapper {
    public static Event toEvent(NewEventDto dto) {
        return new Event(
                dto.getAnnotation(),
                dto.getDescription(),
                LocalDateTime.parse(dto.getEventDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                dto.getPaid(),
                dto.getParticipantLimit(),
                dto.getRequestModeration(),
                dto.getTitle());
    }

    public static EventShortDto toEventDto(Event event) {
        return  new EventShortDto(
                event.getAnnotation(),
                CategoryMapper.toDto(event.getCategory()),
                event.getConfirmedRequests(),
                event.getEventDate().toString(),
                event.getId(),
                UserMapper.toShortDto(event.getInitiator()),
                event.isPaid(),
                event.getTitle(),
                event.getViews());
    }

    public static Collection<EventShortDto> toEventDtoCollection(Collection<Event> eventList) {
        return  eventList.stream()
                .map(EventMapper::toEventDto)
                .collect(Collectors.toList());
    }

    public static Collection<EventFullDto> toEventFullDtoCollection(Collection<Event> eventList) {
        return  eventList.stream()
                .map(EventMapper::toEventFullDto)
                .collect(Collectors.toList());
    }

    public static EventFullDto toEventFullDto(Event event) {
        EventFullDto dto = new EventFullDto(
                event.getAnnotation(),
                CategoryMapper.toDto(event.getCategory()),
                event.getConfirmedRequests(),
                event.getCreatedOn().toString(),
                event.getDescription(),
                event.getEventDate().toString(),
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
