package ru.practicum.ewm.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.controllers.apis.authorizedusers.dtos.mappers.RequestMapper;
import ru.practicum.ewm.controllers.apis.authorizedusers.dtos.requests.ParticipationRequestDto;
import ru.practicum.ewm.errors.Error;
import ru.practicum.ewm.exceptions.BadRequestException;
import ru.practicum.ewm.exceptions.ConflictException;
import ru.practicum.ewm.exceptions.ForbiddenException;
import ru.practicum.ewm.exceptions.NotFoundException;
import ru.practicum.ewm.models.Event;
import ru.practicum.ewm.models.EventState;
import ru.practicum.ewm.models.Request;
import ru.practicum.ewm.models.RequestState;
import ru.practicum.ewm.models.User;
import ru.practicum.ewm.repositories.EventCustomRepository;
import ru.practicum.ewm.repositories.EventRepository;
import ru.practicum.ewm.repositories.RequestRepository;
import ru.practicum.ewm.repositories.UserRepository;
import ru.practicum.ewm.services.EventService;
import ru.practicum.ewm.services.RequestService;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Класс сервиса для работы с запросами на участие в событиях, реализующий интерфейс {@link RequestService}
 *
 * @see ParticipationRequestDto
 * @since 1.0
 */
@Service
@Slf4j
public class RequestServiceImpl implements RequestService {
    /**
     * Интерфейс для работы с репозиторием запросов на участие в событии, наследующий {@link JpaRepository}
     *
     * @since 1.0
     */
    private final RequestRepository requestRepository;

    /**
     * Интерфейс для работы с репозиторием пользователей, наследующий {@link JpaRepository}
     *
     * @since 1.0
     */
    private final UserRepository userRepository;

    /**
     * Интерфейс для работы с репозиторием категорий событий, наследующий {@link JpaRepository} и
     * {@link EventCustomRepository}
     *
     * @since 1.0
     */
    private final EventRepository eventRepository;

    /**
     * Интерфейс сервиса для работы с событиями
     *
     * @since 1.0
     */
    private final EventService eventService;

    @Autowired
    public RequestServiceImpl(RequestRepository requestRepository, UserRepository userRepository,
                              EventRepository eventRepository, EventService eventService) {
        this.requestRepository = requestRepository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.eventService = eventService;
    }


    @Override
    public Collection<ParticipationRequestDto> getEventRequests(int userId, int eventId) {
        User user = findUserById(userId);
        Event event = eventService.getEventById(eventId);
        log.info("Поиск запросов на участие в событии id{} пользователя id{}.", userId, eventId);
        return RequestMapper.toDtoCollection(requestRepository.getRequestsByEventAndEventInitiator(event, user));
    }


    @Override
    public ParticipationRequestDto confirmEventRequest(int userId, int eventId, int reqId) {
        User user = findUserById(userId);
        Event event = getEventByIdAndInitiator(eventId, user);
        Request request = getRequestById(reqId);
        if (request.getEvent().equals(event)) {
            if (!event.isRequestModeration() || event.getParticipantLimit() == 0) {
                request.setState(RequestState.CONFIRMED);
                log.info("Запрос id{} на участие в событии id{} подтверждён.", reqId, eventId);
                return RequestMapper.toDto(requestRepository.save(request));
            }
            if (request.getState().equals(RequestState.CONFIRMED)) {
                log.error("Запрос id{} на участие в событии id{} уже подтвержден.", reqId, eventId);
                throw new BadRequestException(List.of(
                        new Error("state", "не должно быть CONFIRM").toString()),
                        "Невозможно подтвердить запрос на участие в событии.",
                        String.format("Запрос id%d на участие в событии id%d уже подтвержден.", reqId, eventId));
            }
            if (event.getParticipantLimit() == requestRepository.getConfirmedRequests(eventId)) {
                log.error("Превышен лимит запросов на участие в событии id{}.", eventId);
                throw new ForbiddenException(List.of(
                        new Error("confirmedRequests", "имеется ограничение по количеству участников")
                                .toString()),
                        "Невозможно подтвердить запрос на участие в событии.",
                        String.format("Превышен лимит запросов на участие в событии id%d.", eventId));
            }
            if (event.getParticipantLimit() + 1 == requestRepository.getConfirmedRequests(eventId)) {
                request.setState(RequestState.CONFIRMED);
                List<Request> pendingRequests = requestRepository.findRequestsByEventAndState(event,
                        RequestState.PENDING);
                for (Request req : pendingRequests) {
                    req.setState(RequestState.CANCELED);
                }
                requestRepository.saveAll(pendingRequests);
                event.setIsAvailable(false);
                eventRepository.save(event);
            } else {
                request.setState(RequestState.CONFIRMED);
            }
            log.info("Запрос id{} на участие в событии id{} подтверждён.", reqId, eventId);
            return RequestMapper.toDto(requestRepository.save(request));
        } else {
            log.error("Запрос id{} на участие в событии id{} не найден. Проверьте соответствие запроса " +
                    "событию.", reqId, eventId);
            throw new BadRequestException(List.of(
                    new Error("reqId", "неверное значение " + reqId).toString(),
                    new Error("eventId", "неверное значение " + eventId).toString()),
                    "Невозможно подтвердить запрос на участие в событии.",
                    String.format("Запрос id%d на участие в событии id%d не найден. Проверьте соответствие запроса " +
                            "событию.", reqId, eventId));
        }
    }

    @Override
    public ParticipationRequestDto rejectEventRequest(int userId, int eventId, int reqId) {
        User user = findUserById(userId);
        Event event = getEventByIdAndInitiator(eventId, user);
        Request request = getRequestById(reqId);
        if (request.getEvent().equals(event)) {
            if (request.getState().equals(RequestState.REJECTED)) {
                throw new BadRequestException(List.of(
                        new Error("state", "не должно быть REJECT").toString()),
                        "Невозможно отменить запрос на участие в событии.",
                        String.format("Запрос id%d на участие в событии id%d уже отменен.", reqId, eventId));
            }
            request.setState(RequestState.REJECTED);
            log.info("Запрос id{} на участие в событии id{} отменён.", reqId, eventId);
            return RequestMapper.toDto(requestRepository.save(request));
        } else {
            throw new BadRequestException(List.of(
                    new Error("reqId", "неверное значение " + reqId).toString(),
                    new Error("eventId", "неверное значение " + eventId).toString()),
                    "Невозможно отменить запрос на участие в событии.",
                    String.format("Запрос id%d на участие в событии id%d не найден. Проверьте соответствие запроса " +
                            "событию.", reqId, eventId));
        }
    }

    @Override
    public Collection<ParticipationRequestDto> getUserRequests(int userId) {
        User user = findUserById(userId);
        log.info("Поиск заявок пользователя id{} на участие в чужих событиях.", userId);
        return RequestMapper.toDtoCollection(requestRepository.findRequestsByRequester(user));
    }

    @Override
    public ParticipationRequestDto addRequest(int userId, int eventId) {
        Event event = eventService.getEventById(eventId);
        if (!event.getIsAvailable()) {
            log.error("Достигнуто максимально возможное количество участников.");
            throw new ConflictException(List.of(
                    new Error("participantLimit", "превышено максимальное значение " +
                            event.getParticipantLimit()).toString()),
                    String.format("Невозможно добавить запрос на участие в событии id%d.", event.getId()),
                    "Достигнуто максимально возможное количество участников.");
        }
        User user = findUserById(userId);
        if (requestRepository.getRequestByEventAndRequester(event, user).isPresent()) {
            log.error("Запрос пользователя id{} на участие в событии id{} уже существует.", userId, event.getId());
            throw new ConflictException(List.of(
                    new Error("eventId", "неверное значение " + event.getId()).toString()),
                    String.format("Невозможно добавить запрос на участие в событии id%d.", event.getId()),
                    String.format("Запрос пользователя id%d на участие в событии id%d уже существует.", userId,
                            event.getId()));
        }
        if (event.getInitiator().getId() == userId) {
            log.error("Пользователь id{} не может подать запрос на участие в своем событии id{}.", userId,
                    event.getId());
            throw new ConflictException(List.of(
                    new Error("userId", "неверное значение " + userId).toString()),
                    String.format("Невозможно добавить запрос на участие в событии id%d.", event.getId()),
                    String.format("Пользователь id%d не может подать запрос на участие в своем событии id%d.", userId,
                            event.getId()));
        }
        if (!event.getState().equals(EventState.PUBLISHED)) {
            log.error("Нельзя участвовать в неопубликованном событии.");
            throw new ConflictException(List.of(
                    new Error("state", "неверное значение " + event.getState()).toString()),
                    String.format("Невозможно добавить запрос на участие в событии id%d.", event.getId()),
                    "Нельзя участвовать в неопубликованном событии.");
        }
        if (event.getParticipantLimit() != 0
                && requestRepository.getConfirmedRequests(event.getId()) == event.getParticipantLimit()) {
            log.error("Достигнуто максимально возможное количество участников.");
            event.setIsAvailable(false);
            eventRepository.save(event);
            throw new ConflictException(List.of(
                    new Error("participantLimit", "превышено максимальное значение " +
                            event.getParticipantLimit()).toString()),
                    String.format("Невозможно добавить запрос на участие в событии id%d.", event.getId()),
                    "Достигнуто максимально возможное количество участников.");
        }
        Request request = new Request(LocalDateTime.now(), event, user, RequestState.PENDING);
        if (!event.isRequestModeration()) {
            request.setState(RequestState.CONFIRMED);
        }
        request.setRequester(user);
        request.setEvent(event);
        Request savedRequest = requestRepository.save(request);
        log.info("Добавлен запрос id{} на участие в событии id{}.", savedRequest.getId(), event.getId());
        return RequestMapper.toDto(savedRequest);
    }

    @Override
    public ParticipationRequestDto cancelRequestByUser(int userId, int requestId) {
        User user = findUserById(userId);
        Optional<Request> request = requestRepository.findRequestByIdAndRequester(requestId, user);
        if (request.isPresent()) {
            if (request.get().getState().equals((RequestState.CANCELED))) {
                log.error("Невозможно отменить заявку со статусом {}.", request.get().getState());
                throw new ConflictException(List.of(
                        new Error("state", "неверное значение " + request.get().getState()).toString()),
                        String.format("Невозможно отменить заявку id%d", requestId),
                        String.format("Невозможно отменить заявку со статусом %s.", request.get().getState()));
            }
            request.get().setState(RequestState.CANCELED);
            Request savedRequest = requestRepository.save(request.get());
            log.info(String.format("Отменён запрос id%d на участие в событии id%d.", requestId,
                    request.get().getEvent().getId()));
            return RequestMapper.toDto(savedRequest);
        } else {
            log.error("Не найдена заявка id{} пользователя id{}.", requestId, userId);
            throw new NotFoundException(List.of(
                    new Error("requestId", "неверное значение " + requestId).toString(),
                    new Error("userId", "неверное значение " + userId).toString()),
                    String.format("Невозможно отменить заявку id%d", requestId),
                    String.format("Не найдена заявка id%d пользователя id%d.", requestId, userId));
        }
    }

    /**
     * Метод позволяет получить пользователя по идентификатору из репозитория
     *
     * @param id идентификатор пользователя
     * @return пользователь
     * @since 1.0
     */
    private User findUserById(int id) {
        return userRepository.findById(id).get();
    }

    /**
     * Метод позволяет получить событие по идентификатору и организатору
     *
     * @param eventId идентификатор события
     * @param user    организатор события
     * @return событие
     * @since 1.0
     */
    private Event getEventByIdAndInitiator(int eventId, User user) {
        return eventRepository.findEventByIdAndInitiator(eventId, user).orElseThrow(() ->
                new NotFoundException(List.of(
                        new Error("eventId", "неверное значение " + eventId).toString()),
                        "Невозможно получить событие.",
                        String.format("Событие id%d не найдено у пользователя id%d.", eventId, user.getId())));
    }

    /**
     * Метод позволяет получить запрос на участие в событии по идентификатору
     *
     * @param reqId идентификатор запроса на участие в событии
     * @return запрос на участие в событии
     * @since 1.0
     */
    private Request getRequestById(int reqId) {
        return requestRepository.findById(reqId).orElseThrow(() -> new NotFoundException(List.of(
                new Error("reqId", "неверное значение " + reqId).toString()),
                "Невозможно получить запрос на участие в событии.",
                String.format("Запрос id%d не найден.", reqId)));
    }

    @Override
    public boolean checkUserEventParticipation(Event event, User user) {
        Optional<Request> requestOptional = requestRepository.getRequestByEventAndRequesterAndState(event, user,
                RequestState.CONFIRMED);
        return requestOptional.isPresent();
    }

    @Override
    public Integer getConfirmedRequests(int eventId) {
        return requestRepository.getConfirmedRequests(eventId);
    }
}
