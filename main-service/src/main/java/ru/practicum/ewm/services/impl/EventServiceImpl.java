package ru.practicum.ewm.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.clients.EventClient;
import ru.practicum.ewm.errors.Error;
import ru.practicum.ewm.exceptions.BadRequestException;
import ru.practicum.ewm.exceptions.ConflictException;
import ru.practicum.ewm.exceptions.ForbiddenException;
import ru.practicum.ewm.exceptions.NotFoundException;
import ru.practicum.ewm.models.*;
import ru.practicum.ewm.models.dto.events.*;
import ru.practicum.ewm.models.dto.likes.LikeDto;
import ru.practicum.ewm.models.dto.mappers.EventMapper;
import ru.practicum.ewm.models.dto.stats.ViewStatsDto;
import ru.practicum.ewm.repositories.*;
import ru.practicum.ewm.services.EventService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final RequestRepository requestRepository;
    private final LikeRepository likeRepository;

    private final EventClient eventClient;

    private static final String APP_NAME = "ewm-main-service";
    private static final String START = "1970-01-01 00:00:00";
    private static final String END = "2500-12-31 23:59:59";

    @Autowired
    public EventServiceImpl(EventRepository eventRepository, UserRepository userRepository,
                            CategoryRepository categoryRepository, RequestRepository requestRepository,
                            LikeRepository likeRepository, EventClient eventClient) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.requestRepository = requestRepository;
        this.likeRepository = likeRepository;
        this.eventClient = eventClient;
    }

    @Override
    public Collection<EventShortDto> getAllEvents(String text, Integer[] categories, boolean paid, String rangeStart,
                                                  String rangeEnd, boolean onlyAvailable, String sort, int from,
                                                  int size, HttpServletRequest request) {
        List<Event> returnedEvents = eventRepository.getAllEvents(text, categories, paid, rangeStart,
                rangeEnd, onlyAvailable, from, size);
        for (Event e : returnedEvents) {
            addViews("/events/" + e.getId(), e);
            calculateEventLikesAndDislikes(e, e.getInitiator().getId());
        }
        eventClient.addHit(APP_NAME, request.getRequestURI(), request.getRemoteAddr());
        if (sort.equals("VIEWS")) {
            return EventMapper.toEventDtoCollection(returnedEvents)
                    .stream()
                    .sorted(Comparator.comparing(EventShortDto::getViews))
                    .collect(Collectors.toList());
        } else if (sort.equals("EVENT_DATE")) {
            return EventMapper.toEventDtoCollection(returnedEvents)
                    .stream()
                    .sorted(Comparator.comparing(EventShortDto::getEventDate))
                    .collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public EventFullDto getFullEventById(int id, HttpServletRequest request) {
        Event event = getEventById(id);
        eventClient.addHit(APP_NAME, request.getRequestURI(), request.getRemoteAddr());
        if (!event.getState().equals(EventState.PUBLISHED)) {
            log.error("Невозможно получить событие id{} со статусом {}.", id, event.getState());
            throw new ForbiddenException(List.of(
                    new Error("state", "должно быть PUBLISHED").toString()),
                    String.format("Невозможно получить событие id%d", id),
                    String.format("Статус события id%d - %S", id, event.getState())
            );
        }
        addViews("/events/" + id, event);
        calculateEventLikesAndDislikes(event, event.getInitiator().getId());
        return EventMapper.toEventFullDto(event);
    }

    @Override
    public Event getEventById(int id) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new NotFoundException(List.of(
                new Error("id", "неверное значение " + id).toString()),
                "Невозможно получить событие.",
                String.format("Событие с id%d не найдено.", id)));
        event.setConfirmedRequests(requestRepository.getConfirmedRequests(id));
        addViews("/events/" + id, event);
        return event;
    }

    @Override
    public EventFullDto addEvent(int userId, NewEventDto eventDto) {
        Event event = EventMapper.toEvent(eventDto);
        if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new BadRequestException(List.of(
                    new Error("eventDate", "должно быть не раньше, чем через 2 часа от текущего момента")
                            .toString()),
                    String.format("Невозможно добавить событие id%d", event.getId()),
                    String.format("Дата начала события %s", event.getEventDate().toString()));
        }
        Category category = categoryRepository.findById(eventDto.getCategory()).orElseThrow(() ->
                new NotFoundException(List.of(
                        new Error("id", "неверное значение " + eventDto.getCategory()).toString()),
                        "Невозможно получить категорию.",
                        String.format("Категория с id%d не найдена.", eventDto.getCategory())));
        User user = getUserById(userId);
        event.setCategory(category);
        event.setInitiator(user);
        event.setCreatedOn(LocalDateTime.now());
        event.setState(EventState.PENDING);
        event.setIsAvailable(true);
        Event savedEvent = eventRepository.save(event);
        log.info(String.format("Добавлено новое событие id%d.", savedEvent.getId()));
        return EventMapper.toEventFullDto(savedEvent);
    }

    @Override
    public Collection<EventFullDto> searchEventsToAdmin(Integer[] users, String[] states, Integer[] categories,
                                                        String rangeStart, String rangeEnd, int from, int size) {
        log.info("Поиск событий для администратора по запрошенным параметрам.");
        EventState[] states1 = new EventState[states.length];
        for (int i = 0; i < states.length; i++) {
            states1[i] = EventState.valueOf(states[i]);
        }
        List<Event> events = eventRepository.searchEventsToAdmin(users, states1, categories, rangeStart, rangeEnd,
                from, size);
        for (Event e : events) {
            e.setConfirmedRequests(requestRepository.getConfirmedRequests(e.getId()));
            addViews("/events/" + e.getId(), e);
            calculateEventLikesAndDislikes(e, e.getInitiator().getId());
        }
        return EventMapper.toEventFullDtoCollection(events);
    }

    @Override
    public EventFullDto updateEventByAdmin(int eventId, AdminUpdateEventRequestDto eventDto) {
        Event event = getEventById(eventId);
        updateAvailableFields(eventDto, event);
        if (eventDto.getEventDate() != null) {
            event.setEventDate(LocalDateTime.parse(eventDto.getEventDate(), EventMapper.FORMATTER));
        }
        if (eventDto.getRequestModeration() != null) {
            event.setRequestModeration(eventDto.getRequestModeration());
        }
        Event updatedEvent = eventRepository.save(event);
        addViews("/events/" + eventId, updatedEvent);
        calculateEventLikesAndDislikes(event, event.getInitiator().getId());
        log.info(String.format("Администратором обновлено событие id%d.", eventId));
        return EventMapper.toEventFullDto(updatedEvent);
    }

    @Override
    public EventFullDto publishEvent(int eventId) {
        Event event = getEventById(eventId);
        if (event.getState().equals(EventState.PENDING)) {
            if (event.getEventDate().isAfter(LocalDateTime.now().plusHours(1))) {
                event.setPublishedOn(LocalDateTime.now());
                event.setState(EventState.PUBLISHED);
                event.setConfirmedRequests(0);
                event.setViews(0);
                log.info("Опубликовано событие id{}.", eventId);
                return EventMapper.toEventFullDto(eventRepository.save(event));
            } else {
                log.error("Невозможно опубликовать событие id{}, дата начала события должна быть не раньше, чем через" +
                        " час от момента публикации.", eventId);
                throw new BadRequestException(List.of(
                        new Error("eventDate",
                                "должно быть не раньше, чем через час от момента публикации").toString()),
                        "Невозможно опубликовать событие.",
                        String.format("Дата начала события id%d равна %s.", eventId, event.getEventDate().toString()));
            }
        } else {
            log.error("Невозможно опубликовать событие со статусом {}.", event.getState());
            throw new ForbiddenException(List.of(
                    new Error("state", "должно быть PENDING").toString()),
                    "Невозможно опубликовать событие.",
                    String.format("Статус события %S.", event.getState()));
        }
    }

    @Override
    public EventFullDto rejectEvent(int eventId) {
        Event event = getEventById(eventId);
        if (event.getState().equals(EventState.REJECT) || event.getState().equals(EventState.PUBLISHED)) {
            log.error("Невозможно отклонить событие со статусом {}.", event.getState());
            throw new ForbiddenException(List.of(
                    new Error("state", "должно быть PENDING").toString()),
                    "Невозможно отменить событие id" + eventId,
                    String.format("Событие id%d имеет статус %s.", eventId, event.getState()));
        }
        event.setState(EventState.CANCELED);
        event.setConfirmedRequests(0);
        event.setViews(0);
        log.info("Отклонено событие id{}.", eventId);
        return EventMapper.toEventFullDto(eventRepository.save(event));
    }

    @Override
    public Collection<EventShortDto> getUserEvents(int userId, int from, int size) {
        User user = getUserById(userId);
        Pageable page = PageRequest.of(from, size);
        log.info("Запрошены все события пользователя id{} с {} в размере {}.", userId, from, size);
        List<Event> events = eventRepository.findEventsByInitiator(user, page);
        for (Event e : events) {
            e.setConfirmedRequests(requestRepository.getConfirmedRequests(e.getId()));
            addViews("/events/" + e.getId(), e);
            calculateEventLikesAndDislikes(e, userId);
        }
        return EventMapper.toEventDtoCollection(events);
    }

    @Override
    public EventFullDto updateUserEvent(int userId, UpdateEventRequestDto eventDto) {
        User user = getUserById(userId);
        Event event = getEventByIdAndUser(eventDto.getEventId(), user);
        if (!(event.getState().equals(EventState.CANCELED) || event.getState().equals(EventState.PENDING))) {
            log.error("Невозможно обновить событие id{} со статусом {}.", eventDto.getEventId(),
                    event.getState());
            throw new ForbiddenException(List.of(
                    new Error("state", "должно быть CANCELED или PENDING").toString()),
                    String.format("Невозможно обновить событие id%d", eventDto.getEventId()),
                    String.format("Статус события id%d - %S", eventDto.getEventId(),
                            event.getState())
            );
        }
        if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new BadRequestException(List.of(
                    new Error("eventDate", "дата начала события не должна быть раньше, чем через 2 часа " +
                            "от текущего момента").toString()),
                    "Невозможно обновить событие.",
                    String.format("Дата начала события id%d раньше, чем через 2 часа от текущего момента. Дата %s.",
                            eventDto.getEventId(), eventDto.getEventDate()));
        }
        updateAvailableFields(eventDto, event);
        if (eventDto.getEventDate() != null) {
            event.setEventDate(LocalDateTime.parse(eventDto.getEventDate(), EventMapper.FORMATTER));
        }
        if (event.getState().equals(EventState.CANCELED)) {
            event.setState(EventState.PENDING);
        }
        event.setConfirmedRequests(requestRepository.getConfirmedRequests(eventDto.getEventId()));
        Event updatedEvent = eventRepository.save(event);
        addViews("/events/" + event.getId(), updatedEvent);
        calculateEventLikesAndDislikes(event, userId);
        log.info(String.format("Обновлено событие id%d пользователя id%d.", eventDto.getEventId(), userId));
        return EventMapper.toEventFullDto(updatedEvent);
    }

    private void updateAvailableFields(EventDto eventDto, Event event) {
        if (eventDto.getAnnotation() != null) {
            event.setAnnotation(eventDto.getAnnotation());
        }
        if (eventDto.getCategory() != null) {
            Category category = categoryRepository.findById(eventDto.getCategory()).orElseThrow(() ->
                    new NotFoundException(List.of(
                            new Error("id", "неверное значение " + eventDto.getCategory()).toString()),
                            "Невозможно получить категорию.",
                            String.format("Категория с id%d не найдена.", eventDto.getCategory())));
            event.setCategory(category);
        }
        if (eventDto.getDescription() != null) {
            event.setDescription(eventDto.getDescription());
        }
        if (eventDto.getPaid() != null) {
            event.setPaid(eventDto.getPaid());
        }
        if (eventDto.getParticipantLimit() != null) {
            event.setParticipantLimit(eventDto.getParticipantLimit());
        }
        if (eventDto.getTitle() != null) {
            event.setTitle(eventDto.getTitle());
        }
    }

    @Override
    public EventFullDto getUserEvent(int userId, int eventId) {
        User user = getUserById(userId);
        Event event = getEventByIdAndUser(eventId, user);
        event.setConfirmedRequests(requestRepository.getConfirmedRequests(eventId));
        log.info("Запрошено событие id{} пользователя id{}.", eventId, userId);
        addViews("/events/" + eventId, event);
        calculateEventLikesAndDislikes(event, userId);
        return (EventMapper.toEventFullDto(event));
    }

    @Override
    public EventFullDto cancelEventByUser(int userId, int eventId) {
        User user = getUserById(userId);
        Event event = getEventByIdAndUser(eventId, user);
        if (!event.getState().equals(EventState.PENDING)) {
            log.error("Невозможно обновить событие id{} со статусом {}.", event.getId(),
                    event.getState());
            throw new ForbiddenException(List.of(
                    new Error("state", "должно быть PENDING").toString()),
                    String.format("Невозможно обновить событие id%d", event.getId()),
                    String.format("Статус события id%d - %S", event.getId(),
                            event.getState())
            );
        }
        event.setState(EventState.CANCELED);
        event.setConfirmedRequests(0);
        event.setViews(0);
        Event savedEvent = eventRepository.save(event);
        log.info("Отменено событие id{} пользователя id{}.", eventId, userId);
        return EventMapper.toEventFullDto(savedEvent);
    }

    private User getUserById(int id) {
        return userRepository.findById(id).get();
    }

    private Event getEventByIdAndUser(int eventId, User user) {
        Optional<Event> eventOpt = eventRepository.findEventByIdAndInitiator(eventId, user);
        if (eventOpt.isEmpty()) {
            log.error("Событие id{} не найдено у пользователя id{}.", eventId, user.getId());
            throw new NotFoundException(List.of(
                    new Error("id", "неверное значение " + eventId).toString()),
                    "Невозможно получить событие.",
                    String.format("Событие с id%d не найдено у пользователя id%d.", eventId, user.getId()));
        }
        return eventOpt.get();
    }

    private void addViews(String uri, Event event) {
        ViewStatsDto[] views = eventClient.getStats(START, END, new String[]{uri}, false);
        if (views.length == 0) {
            event.setViews(0);
        } else {
            event.setViews(views[0].getHits());
        }
    }

    @Override
    public EventShortDto addLike(int userId, int eventId) {
        User user = getUserById(userId);
        Event event = getEventById(eventId);
        if (userId == event.getInitiator().getId()) {
            log.error("Пользователь id{} не может поставить like своему событию id{}.", userId, eventId);
            throw new ForbiddenException(List.of(
                    new Error("userId", "неверное значение.").toString()),
                    String.format("Невозможно поставить like событию id%d.", eventId),
                    String.format("Пользователь id%d не может поставить like своему событию id%d.", userId, eventId));
        }
        Optional<Like> like = likeRepository.findByUserAndEventAndIsLikeIsTrue(user, event);
        if (like.isPresent()) {
            log.error("Пользователь id{} уже поставил like событию id{}.", userId, eventId);
            throw new ConflictException(List.of(
                    new Error("eventId", "неверное значение").toString()),
                    String.format("Невозможно поставить like событию id%d.", eventId),
                    String.format("Пользователь id%d уже поставил like событию id%d.", userId, eventId));
        }
        Optional<Like> dislike = likeRepository.findByUserAndEventAndIsLikeIsFalse(user, event);
        if (dislike.isPresent()) {
            log.error("Пользователь id{} уже поставил dislike событию id{}.", userId, eventId);
            throw new ConflictException(List.of(
                    new Error("eventId", "неверное значение.").toString()),
                    String.format("Невозможно поставить like событию id%d.", eventId),
                    String.format("Пользователь id%d уже поставил dislike событию id%d.", userId, eventId));
        }
        likeRepository.save(new Like(user, event, true));
        calculateEventLikesAndDislikes(event, event.getInitiator().getId());
        log.info("Пользователь id{} поставил like событию id{}.", userId, eventId);
        return EventMapper.toEventDto(event);
    }

    @Override
    public List<LikeDto> getEventLikesDto(Integer userId, int eventId) {
        return EventMapper.likesToDtoCollection(getEventLikes(userId, eventId));
    }

    private List<Like> getEventLikes(Integer userId, int eventId) {
        Event event = getEventById(eventId);
        if (userId == null) {
            log.info("Администратор запросил все лайки события id{}.", eventId);
            return likeRepository.findAllByEventAndIsLikeIsTrue(event);
        }
        if (userId != event.getInitiator().getId()) {
            log.error("Пользователь id{} не является организатором события id{}.", userId, eventId);
            throw new ForbiddenException(List.of(
                    new Error("userId", "неверное значение.").toString()),
                    String.format("Невозможно получить список лайков событию id%d.", eventId),
                    String.format("Пользователь id%d не является организатором события id%d.", userId, eventId));
        }
        log.info("Пользователь id{} запросил все лайки своего события id{}.", userId, eventId);
        return likeRepository.findAllByEventAndIsLikeIsTrue(event);
    }

    @Override
    public EventShortDto addDislike(int userId, int eventId) {
        User user = getUserById(userId);
        Event event = getEventById(eventId);
        if (userId == event.getInitiator().getId()) {
            log.error("Пользователь id{} не может поставить dislike своему событию id{}.", userId, eventId);
            throw new ForbiddenException(List.of(
                    new Error("userId", "неверное значение.").toString()),
                    String.format("Невозможно поставить dislike событию id%d.", eventId),
                    String.format("Пользователь id%d не может поставить dislike своему событию id%d.", userId,
                            eventId));
        }
        Optional<Like> dislike = likeRepository.findByUserAndEventAndIsLikeIsFalse(user, event);
        if (dislike.isPresent()) {
            log.error("Пользователь id{} уже поставил dislike событию id{}.", userId, eventId);
            throw new ConflictException(List.of(
                    new Error("eventId", "неверное значение.").toString()),
                    String.format("Невозможно поставить dislike событию id%d.", eventId),
                    String.format("Пользователь id%d уже поставил dislike событию id%d.", userId, eventId));
        }
        Optional<Like> like = likeRepository.findByUserAndEventAndIsLikeIsTrue(user, event);
        if (like.isPresent()) {
            log.error("Пользователь id{} уже поставил like событию id{}.", userId, eventId);
            throw new ConflictException(List.of(
                    new Error("eventId", "неверное значение").toString()),
                    String.format("Невозможно поставить dislike событию id%d.", eventId),
                    String.format("Пользователь id%d уже поставил like событию id%d.", userId, eventId));
        }
        likeRepository.save(new Like(user, event, false));
        calculateEventLikesAndDislikes(event, event.getInitiator().getId());
        log.info("Пользователь id{} поставил dislike событию id{}.", userId, eventId);
        return EventMapper.toEventDto(event);
    }

    @Override
    public List<LikeDto> getEventDislikesDto(Integer userId, int eventId) {
        return EventMapper.likesToDtoCollection(getEventDislikes(userId, eventId));
    }

    private List<Like> getEventDislikes(Integer userId, int eventId) {
        Event event = getEventById(eventId);
        if (userId == null) {
            log.info("Администратор запросил все дизлайки события id{}.", eventId);
            return likeRepository.findAllByEventAndIsLikeIsFalse(event);
        }
        if (userId != event.getInitiator().getId()) {
            log.error("Пользователь id{} не является организатором события id{}.", userId, eventId);
            throw new ForbiddenException(List.of(
                    new Error("userId", "неверное значение.").toString()),
                    String.format("Невозможно получить список дизлайков событию id%d.", eventId),
                    String.format("Пользователь id%d не является организатором события id%d.", userId, eventId));
        }
        log.info("Пользователь id{} запросил все дизлайки своего события id{}.", userId, eventId);
        return likeRepository.findAllByEventAndIsLikeIsFalse(event);
    }

    private void calculateEventRating(Event event) {
        float rating;
        float likes = event.getLikes();
        float dislikes = event.getDislikes();
        if (likes > 0 && dislikes == 0) {
            rating = 5;
        } else if ((dislikes > 0 && likes == 0) || (likes == dislikes)) {
            rating = 0;
        } else {
            rating = likes / dislikes;
        }
        event.setRating(rating);
    }

    private void calculateEventLikesAndDislikes(Event event, int userId) {
        List<Like> likes = getEventLikes(userId, event.getId());
        List<Like> dislikes = getEventDislikes(userId, event.getId());
        if (likes == null || likes.isEmpty()) {
            event.setLikes(0);
        } else {
            event.setLikes(likes.size());
        }
        if (dislikes == null || dislikes.isEmpty()) {
            event.setDislikes(0);
        } else {
            event.setDislikes(dislikes.size());
        }
        calculateEventRating(event);
    }
}
