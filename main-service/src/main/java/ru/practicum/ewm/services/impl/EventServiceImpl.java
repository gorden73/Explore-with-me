package ru.practicum.ewm.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.controllers.apis.admins.dtos.events.AdminUpdateEventRequestDto;
import ru.practicum.ewm.controllers.apis.authorizedusers.dtos.events.EventDto;
import ru.practicum.ewm.controllers.apis.authorizedusers.dtos.events.NewEventDto;
import ru.practicum.ewm.controllers.apis.authorizedusers.dtos.events.UpdateEventRequestDto;
import ru.practicum.ewm.controllers.apis.authorizedusers.dtos.mappers.EventMapper;
import ru.practicum.ewm.clients.BaseClient;
import ru.practicum.ewm.clients.EventClient;
import ru.practicum.ewm.errors.Error;
import ru.practicum.ewm.exceptions.BadRequestException;
import ru.practicum.ewm.exceptions.ForbiddenException;
import ru.practicum.ewm.exceptions.NotFoundException;
import ru.practicum.ewm.models.*;
import ru.practicum.ewm.models.dtos.events.EventFullDto;
import ru.practicum.ewm.models.dtos.events.EventShortDto;
import ru.practicum.ewm.models.dtos.stats.ViewStatsDto;
import ru.practicum.ewm.repositories.*;
import ru.practicum.ewm.services.EventService;
import ru.practicum.ewm.services.LikeService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Класс сервиса для работы с событиями, реализующий интерфейс {@link EventService}
 *
 * @see Event
 * @see EventShortDto
 * @see EventFullDto
 * @since 1.0
 */
@Service
@Slf4j
public class EventServiceImpl implements EventService {
    /**
     * Интерфейс для работы с репозиторием категорий событий, наследующий {@link JpaRepository} и
     * {@link EventCustomRepository}
     *
     * @since 1.0
     */
    private final EventRepository eventRepository;

    /**
     * Интерфейс для работы с репозиторием пользователей, наследующий {@link JpaRepository}
     *
     * @since 1.0
     */
    private final UserRepository userRepository;

    /**
     * Интерфейс для работы с репозиторием категорий событий, наследующий {@link JpaRepository}
     *
     * @since 1.0
     */
    private final CategoryRepository categoryRepository;

    /**
     * Интерфейс для работы с репозиторием запросов на участие в событии, наследующий {@link JpaRepository}
     *
     * @since 1.0
     */
    private final RequestRepository requestRepository;

    /**
     * Класс для работы с просмотрами эндпоинтов событий, наследующий {@link BaseClient}
     *
     * @since 1.0
     */
    private final EventClient eventClient;

    /**
     * Константа идентификатора текущего микросервиса
     *
     * @since 1.0
     */
    private static final String APP_NAME = "ewm-main-service";

    /**
     * Константа даты и времени, не раньше которых нужно выполнять поиск
     *
     * @since 1.0
     */
    private static final String START = "1970-01-01 00:00:00";

    /**
     * Константа даты и времени, не позже которых нужно выполнять поиск
     *
     * @since 1.0
     */
    private static final String END = "2500-12-31 23:59:59";

    @Autowired
    public EventServiceImpl(EventRepository eventRepository, UserRepository userRepository,
                            CategoryRepository categoryRepository, RequestRepository requestRepository,
                            EventClient eventClient) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.requestRepository = requestRepository;
        this.eventClient = eventClient;
    }

    @Override
    public Collection<EventShortDto> getAllEvents(FilterCollector filterCollector, HttpServletRequest request) {
        List<Event> returnedEvents = eventRepository.getAllEvents(filterCollector);
        for (Event e : returnedEvents) {
            addViews("/events/" + e.getId(), e);
            e.setConfirmedRequests(requestRepository.getConfirmedRequests(e.getId()));
        }
        eventClient.addHit(APP_NAME, request.getRequestURI(), request.getRemoteAddr());
        switch (filterCollector.getSort()) {
            case "VIEWS":
                return EventMapper.toEventDtoCollection(returnedEvents)
                        .stream()
                        .sorted(Comparator.comparing(EventShortDto::getViews).reversed())
                        .collect(Collectors.toList());
            case "RATING":
                return EventMapper.toEventDtoCollection(returnedEvents).stream()
                        .sorted(Comparator.comparing(EventShortDto::getRating).reversed())
                        .collect(Collectors.toList());
            default:
                return EventMapper.toEventDtoCollection(returnedEvents);
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
        log.info(String.format("Обновлено событие id%d пользователя id%d.", eventDto.getEventId(), userId));
        return EventMapper.toEventFullDto(updatedEvent);
    }

    /**
     * Метод позволяет обновить все доступные поля события на основе полей Dto события (не равных null)
     *
     * @param eventDto Dto события
     * @param event    событие для обновления
     * @since 1.0
     */
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

    @Override
    public List<Event> findEventsByInitiator(User initiator, int from, int size) {
        Pageable page = PageRequest.of(from, size);
        return eventRepository.findEventsByInitiator(initiator, page);
    }

    /**
     * Метод позволяет получить пользователя по идентификатору из репозитория
     *
     * @param id идентификатор пользователя
     * @return пользователь
     * @since 1.0
     */
    private User getUserById(int id) {
        return userRepository.findById(id).get();
    }

    /**
     * Метод позволяет получить событие по идентификатору и организатору события
     *
     * @param eventId идентификатор события
     * @param user    организатор события
     * @return событие
     * @since 1.0
     */
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

    /**
     * Метод позволяет добавить просмотры в событие на основе полученной статистики просмотров эндпоинтов
     *
     * @param uri   URI, для которого запрашивается статистика
     * @param event событие, в которое добавляются просмотры
     * @since 1.0
     */
    private void addViews(String uri, Event event) {
        ViewStatsDto[] views = eventClient.getStats(START, END, new String[]{uri}, false);
        if (views.length == 0) {
            event.setViews(0);
        } else {
            event.setViews(views[0].getHits());
        }
    }
}
