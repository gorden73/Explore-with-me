package ru.practicum.ewm.apis.authorizedusers.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.apis.authorizedusers.dtos.requests.ParticipationRequestDto;
import ru.practicum.ewm.services.RequestService;

import java.util.Collection;

/**
 * Класс для работы авторизованного пользователя с запросами на участие в событии
 * @since 1.0
 */
@RestController
@RequestMapping(path = "/users/{userId}/")
public class AuthorizedUserRequestController {
    /**
     * Сервис для работы с запросами на участие в событии
     * @since 1.0
     */
    private final RequestService requestService;

    @Autowired
    public AuthorizedUserRequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    /**
     * Метод позволяет получить список запросов на участие в событии по его идентификатору
     * @param userId идентификатор пользователя {@link ru.practicum.ewm.models.User}
     * @param eventId идентификатор события {@link ru.practicum.ewm.models.Event}
     * @return список запросов на участие в событии
     * @since 1.0
     */
    @GetMapping("/events/{eventId}/requests")
    public Collection<ParticipationRequestDto> getEventRequests(@PathVariable int userId,
                                                                @PathVariable int eventId) {
        return requestService.getEventRequests(userId, eventId);
    }

    /**
     * Метод позволяет пользователю по идентификатору подтвердить запрос на участие в его событии
     * @param userId идентификатор пользователя {@link ru.practicum.ewm.models.User}
     * @param eventId идентификатор события {@link ru.practicum.ewm.models.Event}
     * @param reqId идентификатор запроса {@link ru.practicum.ewm.models.Request}
     * @return информация о подтвержденном запросе
     * @since 1.0
     */
    @PatchMapping("/events/{eventId}/requests/{reqId}/confirm")
    public ParticipationRequestDto confirmEventRequest(@PathVariable int userId,
                                                       @PathVariable int eventId,
                                                       @PathVariable int reqId) {
        return requestService.confirmEventRequest(userId, eventId, reqId);
    }

    /**
     * Метод позволяет пользователю по идентификатору отклонить запрос на участие в его событии
     * @param userId идентификатор пользователя {@link ru.practicum.ewm.models.User}
     * @param eventId идентификатор события {@link ru.practicum.ewm.models.Event}
     * @param reqId идентификатор запроса {@link ru.practicum.ewm.models.Request}
     * @return информация об отклоненном запросе
     * @since 1.0
     */
    @PatchMapping("/events/{eventId}/requests/{reqId}/reject")
    public ParticipationRequestDto rejectEventRequest(@PathVariable int userId,
                                                      @PathVariable int eventId,
                                                      @PathVariable int reqId) {
        return requestService.rejectEventRequest(userId, eventId, reqId);
    }

    /**
     * Метод позволяет пользователю получить список всех его запросов на участие событиях
     * @param userId идентификатор пользователя {@link ru.practicum.ewm.models.User}
     * @return список всех запросов на участие событиях
     * @since 1.0
     */
    @GetMapping("/requests")
    public Collection<ParticipationRequestDto> getUserRequests(@PathVariable int userId) {
        return requestService.getUserRequests(userId);
    }

    /**
     * Метод позволяет пользователю создать новый запрос на участие в чужом событии
     * @param userId идентификатор пользователя {@link ru.practicum.ewm.models.User}
     * @param eventId идентификатор события {@link ru.practicum.ewm.models.Event}
     * @return информация о созданном запросе
     * @since 1.0
     */
    @PostMapping("/requests")
    public ParticipationRequestDto addRequest(@PathVariable int userId,
                                              @RequestParam int eventId) {
        return requestService.addRequest(userId, eventId);
    }

    /**
     * Метод позволяет пользователю по идентификатору отменить свой запрос на участие в чужом событии
     * @param userId идентификатор пользователя {@link ru.practicum.ewm.models.User}
     * @param requestId идентификатор запроса {@link ru.practicum.ewm.models.Request}
     * @return информация об отмененном запросе
     * @since 1.0
     */
    @PatchMapping("/requests/{requestId}/cancel")
    public ParticipationRequestDto cancelRequestByUser(@PathVariable int userId,
                                                       @PathVariable int requestId) {
        return requestService.cancelRequestByUser(userId, requestId);
    }
}
