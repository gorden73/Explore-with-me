package ru.practicum.ewm.controllers.apis.authorizedusers.dtos.mappers;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.controllers.apis.admins.dtos.likes.AdminDislikeDto;
import ru.practicum.ewm.controllers.apis.admins.dtos.likes.AdminLikeDto;
import ru.practicum.ewm.controllers.apis.admins.dtos.mappers.CategoryMapper;
import ru.practicum.ewm.controllers.apis.admins.dtos.mappers.UserMapper;
import ru.practicum.ewm.controllers.apis.authorizedusers.dtos.events.NewEventDto;
import ru.practicum.ewm.controllers.apis.authorizedusers.dtos.likes.DislikeDto;
import ru.practicum.ewm.controllers.apis.authorizedusers.dtos.likes.LikeDto;
import ru.practicum.ewm.models.Event;
import ru.practicum.ewm.models.Like;
import ru.practicum.ewm.models.dtos.events.EventFullDto;
import ru.practicum.ewm.models.dtos.events.EventShortDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
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
                event.getViews(),
                event.getLikes(),
                event.getDislikes(),
                String.format("%.1f", event.getRating()));
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
        EventFullDto dto = new EventFullDto();
        dto.setAnnotation(event.getAnnotation());
        dto.setCategory(CategoryMapper.toDto(event.getCategory()));
        dto.setConfirmedRequests(event.getConfirmedRequests());
        dto.setCreatedOn(event.getCreatedOn().format(FORMATTER));
        dto.setDescription(event.getDescription());
        dto.setEventDate(event.getEventDate().format(FORMATTER));
        dto.setId(event.getId());
        dto.setInitiator(UserMapper.toShortDto(event.getInitiator()));
        dto.setPaid(event.isPaid());
        dto.setParticipantLimit(event.getParticipantLimit());
        dto.setRequestModeration(event.isRequestModeration());
        dto.setState(event.getState().toString());
        dto.setTitle(event.getTitle());
        dto.setViews(event.getViews());
        dto.setLikes(event.getLikes());
        dto.setDislikes(event.getDislikes());
        dto.setRating(String.format("%.1f",event.getRating()));
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

    public static DislikeDto dislikeToDto(Like like) {
        return new DislikeDto(like.getUser().getName());
    }

    public static List<DislikeDto> dislikesToDtoCollection(List<Like> dislikes) {
        return dislikes.stream()
                .map(EventMapper::dislikeToDto)
                .collect(Collectors.toList());
    }

    public static AdminDislikeDto dislikeToAdminDto(Like dislike) {
        return new AdminDislikeDto(
                dislike.getId(),
                UserMapper.toDto(dislike.getUser()),
                EventMapper.toEventFullDto(dislike.getEvent()));
    }

    public static List<AdminDislikeDto> dislikesToAdminDtoCollection(List<Like> dislikes) {
        return dislikes.stream()
                .map(EventMapper::dislikeToAdminDto)
                .collect(Collectors.toList());
    }
}
