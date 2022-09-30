package ru.practicum.ewm.apis.authorizedusers.dtos.mappers;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.apis.admins.dtos.mappers.CategoryMapper;
import ru.practicum.ewm.apis.admins.dtos.mappers.UserMapper;
import ru.practicum.ewm.apis.authorizedusers.dtos.events.NewEventDto;
import ru.practicum.ewm.models.Event;
import ru.practicum.ewm.models.dtos.events.EventFullDto;
import ru.practicum.ewm.models.dtos.events.EventShortDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Класс для преобразования сущности события в Dto и обратно
 *
 * @see Event
 * @since 1.0
 */
@Component
public class EventMapper {
    /**
     * Преобразователь даты и времени в определенный формат
     *
     * @since 1.0
     */
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Метод позволяет преобразовать Dto нового события в сущность
     *
     * @param dto Dto нового события
     * @return сущность события
     * @since 1.0
     */
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

    /**
     * Метод позволяет преобразовать сущность события в Dto с кратким описанием события
     *
     * @param event сущность события
     * @return Dto с кратким описанием события
     * @since 1.0
     */
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

    /**
     * Метод позволяет преобразовать коллекцию сущностей событий в коллекцию Dto с кратким описанием событий
     *
     * @param eventList коллекцию сущностей событий
     * @return коллекцию Dto с кратким описанием событий
     * @since 1.0
     */
    public static Set<EventShortDto> toEventDtoCollection(Collection<Event> eventList) {
        return eventList.stream()
                .map(EventMapper::toEventDto)
                .collect(Collectors.toSet());
    }

    /**
     * Метод позволяет преобразовать коллекцию сущностей событий в коллекцию Dto с полным описанием событий
     *
     * @param eventList коллекцию сущностей событий
     * @return коллекцию Dto с полным описанием событий
     * @since 1.0
     */
    public static Collection<EventFullDto> toEventFullDtoCollection(Collection<Event> eventList) {
        return eventList.stream()
                .map(EventMapper::toEventFullDto)
                .collect(Collectors.toList());
    }

    /**
     * Метод позволяет преобразовать сущность события в Dto с полным описанием события
     *
     * @param event сущность события
     * @return Dto с полным описанием события
     * @since 1.0
     */
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
