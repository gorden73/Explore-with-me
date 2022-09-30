package ru.practicum.ewm.models.dto.mappers;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.models.Event;
import ru.practicum.ewm.models.Like;
import ru.practicum.ewm.models.dto.events.EventFullDto;
import ru.practicum.ewm.models.dto.events.EventShortDto;
import ru.practicum.ewm.models.dto.events.NewEventDto;
import ru.practicum.ewm.models.dto.likes.AdminLikeDto;
import ru.practicum.ewm.models.dto.likes.LikeDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
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
                event.getViews(),
                event.getLikes(),
                event.getDislikes(),
                String.format("%.1f", event.getRating()));
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
                event.getViews(),
                event.getLikes(),
                event.getDislikes(),
                String.format("%.1f",event.getRating()));
        if (event.getPublishedOn() == null) {
            dto.setPublishedOn(null);
        } else {
            dto.setPublishedOn(event.getPublishedOn().toString());
        }
        return dto;
    }

    public static LikeDto likeToDto(Like like) {
        return new LikeDto(like.getUser().getName());
    }

    public static List<LikeDto> likesToDtoCollection(List<Like> likes) {
        return likes.stream()
                .map(EventMapper::likeToDto)
                .collect(Collectors.toList());
    }

    public static AdminLikeDto likeToAdminDto(Like like) {
        return new AdminLikeDto(
                like.getId(),
                UserMapper.toDto(like.getUser()),
                EventMapper.toEventFullDto(like.getEvent()));
    }

    public static List<AdminLikeDto> likesToAdminDtoCollection(List<Like> likes) {
        return likes.stream()
                .map(EventMapper::likeToAdminDto)
                .collect(Collectors.toList());
    }
}
