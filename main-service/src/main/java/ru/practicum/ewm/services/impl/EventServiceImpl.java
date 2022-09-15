package ru.practicum.ewm.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.models.Category;
import ru.practicum.ewm.models.Event;
import ru.practicum.ewm.models.EventState;
import ru.practicum.ewm.models.User;
import ru.practicum.ewm.models.dto.events.*;
import ru.practicum.ewm.models.dto.mappers.EventMapper;
import ru.practicum.ewm.repositories.CategoryRepository;
import ru.practicum.ewm.repositories.EventRepository;
import ru.practicum.ewm.repositories.RequestRepository;
import ru.practicum.ewm.repositories.UserRepository;
import ru.practicum.ewm.services.EventService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Slf4j
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    private final RequestRepository requestRepository;

    @Autowired
    public EventServiceImpl(EventRepository eventRepository, UserRepository userRepository, CategoryRepository categoryRepository, RequestRepository requestRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.requestRepository = requestRepository;
    }

    @Override
    public Collection<EventShortDto> getAllEvents(String text, Integer[] categories, boolean paid, String rangeStart,
                                                  String rangeEnd, boolean onlyAvailable, String sort, int from,
                                                  int size) {
        Pageable page = PageRequest.of(from, size);
        if (rangeStart == null || rangeEnd == null) {
            if (sort.equals("EVENT_DATE")) {
                List<Event> events = eventRepository.getAllEventsSortByEventDate(text, categories,
                        paid, LocalDateTime.now(), onlyAvailable, EventState.PUBLISHED, page);
                for (Event e : events) {
                    e.setConfirmedRequests(requestRepository.getConfirmedRequests(e.getId()));
                }
                // здесь надо сделать запрос в сервис статистики для добавления просмотров
                // в каждое событие
                return EventMapper.toEventDtoCollection(events);
            } else if (sort.equals("VIEWS")) {
                List<Event> events = eventRepository.getAllEventsUnsorted(text, categories, paid,
                        LocalDateTime.now(), onlyAvailable, EventState.PUBLISHED, page);
                for (Event e : events) {
                    e.setConfirmedRequests(requestRepository.getConfirmedRequests(e.getId()));
                }
                // здесь надо сделать запрос в сервис статистики для добавления просмотров
                // в каждое событие и последующую сортировку этих событий по количеству просмотров
                // и надо добавить количество одобренных заявок на участие
                return EventMapper.toEventDtoCollection(events);
            }
        }
        if (sort.equals("EVENT_DATE")) {
            List<Event> events = eventRepository.getAllEventsSortByEventDate(text, categories, paid,
                    LocalDateTime.parse(rangeStart), LocalDateTime.parse(rangeEnd), onlyAvailable,
                    EventState.PUBLISHED, page);
            for (Event e : events) {
                e.setConfirmedRequests(requestRepository.getConfirmedRequests(e.getId()));
            }
            return EventMapper.toEventDtoCollection(events);
            // здесь надо сделать запрос в сервис статистики для добавления просмотров
            // в каждое событие
        } else if (sort.equals("VIEWS")) {
            List<Event> events = eventRepository.getAllEventsUnsorted(text, categories, paid,
                    LocalDateTime.parse(rangeStart), LocalDateTime.parse(rangeEnd), onlyAvailable,
                    EventState.PUBLISHED, page);
            for (Event e : events) {
                e.setConfirmedRequests(requestRepository.getConfirmedRequests(e.getId()));
            }
            // здесь надо сделать запрос в сервис статистики для добавления просмотров
            // в каждое событие и последующую сортировку этих событий по количеству просмотров
            return EventMapper.toEventDtoCollection(events);
        }
        return null;
    }

    @Override
    public EventFullDto getEventById(int id) {
        Event event = eventRepository.findById(id).orElseThrow();// exception needed!!!
        event.setConfirmedRequests(requestRepository.getConfirmedRequests(id));
        // здесь надо сделать запрос в сервис статистики для добавления просмотров
        return EventMapper.toEventFullDto(event);
    }

    @Override
    public EventFullDto addEvent(int userId, NewEventDto eventDto) {
        Event event = EventMapper.toEvent(eventDto);
        if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new IllegalArgumentException(); // exception needed!!!
        }
        Category category = categoryRepository.findById(eventDto.getCategory()).orElseThrow(); // exception needed!!!
        User user = userRepository.findById(userId).orElseThrow(); // exception needed!!!
        event.setCategory(category);
        event.setInitiator(user);
        event.setCreatedOn(LocalDateTime.now());
        event.setState(EventState.PENDING);
        return EventMapper.toEventFullDto(eventRepository.save(event));
    }

    @Override
    public Collection<EventFullDto> searchEventsToAdmin(Integer[] users, String[] states, Integer[] categories,
                                                        String rangeStart, String rangeEnd, int from, int size) {
        Pageable page = PageRequest.of(from, size);
        List<Event> events = eventRepository.searchEventsToAdmin(users, states, categories, rangeStart, rangeEnd, page);
        for (Event e : events) {
            e.setConfirmedRequests(requestRepository.getConfirmedRequests(e.getId()));
        }
        // здесь надо сделать запрос в сервис статистики для добавления просмотров
        return EventMapper.toEventFullDtoCollection(events);
    }

    @Override
    public EventFullDto updateEventByAdmin(int eventId, AdminUpdateEventRequestDto eventDto) {
        Event event = eventRepository.findById(eventId).orElseThrow(); // exception needed
        updateAvailableFields(eventDto, event);
        if (eventDto.getEventDate() != null) {
            event.setEventDate(LocalDateTime.parse(eventDto.getEventDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd, " +
                    "HH:mm:ss")));
        }
        if (eventDto.getRequestModeration() != null) {
            event.setRequestModeration(eventDto.getRequestModeration());
        }
        return EventMapper.toEventFullDto(eventRepository.save(event));
    }

    @Override
    public EventFullDto publishEvent(int eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(); // exception needed
        event.setPublishedOn(LocalDateTime.now());
        event.setState(EventState.PUBLISHED);
        event.setConfirmedRequests(0);
        event.setViews(0);
        return EventMapper.toEventFullDto(eventRepository.save(event));
    }

    @Override
    public EventFullDto rejectEvent(int eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(); // exception needed
        event.setState(EventState.REJECT);
        event.setConfirmedRequests(0);
        event.setViews(0);
        return EventMapper.toEventFullDto(eventRepository.save(event));
    }

    @Override
    public Collection<Integer> getUserEvents(int userId, int from, int size) {
        User user = userRepository.findById(userId).get();// exception needed
        Pageable page = PageRequest.of(from, size);
        return eventRepository.findEventsByInitiator(user, page);
    }

    @Override
    public EventFullDto updateUserEvent(int userId, UpdateEventRequestDto eventDto) throws IllegalAccessException {
        User user = userRepository.findById(userId).get();// exception needed
        Optional<Event> event = eventRepository.findEventByIdAndInitiator(eventDto.getEventId(), user);
        if (event.isEmpty()) {
            throw new NoSuchElementException();// exception needed
        }
        updateAvailableFields(eventDto, event.get());
        if (eventDto.getEventDate() != null
                && LocalDateTime.parse(eventDto.getEventDate()).isAfter(LocalDateTime.now().plusHours(2))) {
            event.get().setEventDate(LocalDateTime.parse(eventDto.getEventDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd," +
                    " " +
                    "HH:mm:ss")));
        }
        event.get().setConfirmedRequests(requestRepository.getConfirmedRequests(eventDto.getEventId()));
        // здесь надо сделать запрос в сервис статистики для добавления просмотров
        return EventMapper.toEventFullDto(eventRepository.save(event.get()));
    }

    private void updateAvailableFields(EventDto eventDto, Event event) {
        if (eventDto.getAnnotation() != null) {
            event.setAnnotation(eventDto.getAnnotation());
        }
        if (eventDto.getCategory() != null) {
            Category category = categoryRepository.findById(eventDto.getCategory()).orElseThrow(); // exception needed
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
        User user = userRepository.findById(userId).get();// exception needed
        Optional<Event> event = eventRepository.findEventByIdAndInitiator(eventId, user);
        if (event.isEmpty()) {
            throw new NoSuchElementException();// exception needed
        }
        event.get().setConfirmedRequests(requestRepository.getConfirmedRequests(eventId));
        // здесь надо сделать запрос в сервис статистики для добавления просмотров
        return (EventMapper.toEventFullDto(event.get()));
    }

    @Override
    public EventFullDto cancelEventByUser(int userId, int eventId) {
        User user = userRepository.findById(userId).get();// exception needed
        Optional<Event> eventOpt = eventRepository.findEventByIdAndInitiator(eventId, user);
        if (eventOpt.isEmpty()) {
            throw new NoSuchElementException();// exception needed
        }
        Event event = eventOpt.get();
        if (!event.getState().equals(EventState.PENDING)) {
            throw new IllegalArgumentException(); // exception needed
        }
        event.setState(EventState.CANCELED);
        event.setConfirmedRequests(0);
        event.setViews(0);
        return EventMapper.toEventFullDto(eventRepository.save(event));
    }
}
