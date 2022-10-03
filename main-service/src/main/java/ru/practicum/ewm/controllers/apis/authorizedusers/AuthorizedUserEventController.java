package ru.practicum.ewm.controllers.apis.authorizedusers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.controllers.apis.authorizedusers.dtos.events.NewEventDto;
import ru.practicum.ewm.controllers.apis.authorizedusers.dtos.events.UpdateEventRequestDto;
import ru.practicum.ewm.models.dtos.events.EventFullDto;
import ru.practicum.ewm.models.dtos.events.EventShortDto;
import ru.practicum.ewm.services.EventService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;

/**
 * Класс для работы авторизованного пользователя с событиями
 *
 * @since 1.0
 */
@RestController
@RequestMapping(path = "/users/{userId}/events")
@Validated
public class AuthorizedUserEventController {
    /**
     * Сервис для работы с событиями
     *
     * @since 1.0
     */
    private final EventService eventService;

    @Autowired
    public AuthorizedUserEventController(EventService eventService) {
        this.eventService = eventService;
    }

    /**
     * Метод позволяет получить краткую информацию о событиях пользователя, подходящих под заданные условия
     *
     * @param userId идентификатор пользователя {@link ru.practicum.ewm.models.User}
     * @param from   количество элементов, которые нужно пропустить для формирования текущего набора(по умолчанию 0)
     * @param size   количество элементов в наборе(по умолчанию 10)
     * @return краткая информация о событиях пользователя, подходящих под заданные условия
     * @since 1.0
     */
    @GetMapping
    public Collection<EventShortDto> getUserEvents(@PathVariable int userId,
                                                   @RequestParam(defaultValue = "0")
                                                   @PositiveOrZero(message = "может быть равно или больше 0") int from,
                                                   @RequestParam(defaultValue = "10")
                                                   @Positive(message = "может быть только больше 0") int size) {
        return eventService.getUserEvents(userId, from, size);
    }

    /**
     * Метод позволяет пользователю обновить своё событие
     *
     * @param userId   идентификатор пользователя {@link ru.practicum.ewm.models.User}
     * @param eventDto объект Dto, описывающий свойства для обновления события {@link UpdateEventRequestDto}
     * @return полная информация по обновленному событию
     * @since 1.0
     */
    @PatchMapping
    public EventFullDto updateUserEvent(@PathVariable int userId,
                                        @Valid @RequestBody UpdateEventRequestDto eventDto) {
        return eventService.updateUserEvent(userId, eventDto);
    }

    /**
     * Метод позволяет пользователю создать новое событие
     *
     * @param userId   идентификатор пользователя {@link ru.practicum.ewm.models.User}
     * @param eventDto объект Dto, описывающий свойства для создания нового события {@link NewEventDto}
     * @return полная информация по новому событию
     * @since 1.0
     */
    @PostMapping
    public EventFullDto addEvent(@PathVariable int userId,
                                 @Valid @RequestBody NewEventDto eventDto) {
        return eventService.addEvent(userId, eventDto);
    }

    /**
     * Метод позволяет пользователю получить по идентификатору своего события полную информацию о нем
     *
     * @param userId  идентификатор пользователя {@link ru.practicum.ewm.models.User}
     * @param eventId идентификатор события {@link ru.practicum.ewm.models.Event}
     * @return полная информация о событии
     * @since 1.0
     */
    @GetMapping("/{eventId}")
    public EventFullDto getUserEvent(@PathVariable int userId,
                                     @PathVariable int eventId) {
        return eventService.getUserEvent(userId, eventId);
    }

    /**
     * Метод позволяет пользователю отменить свое неопубликованное событие
     *
     * @param userId  идентификатор пользователя {@link ru.practicum.ewm.models.User}
     * @param eventId идентификатор события {@link ru.practicum.ewm.models.Event}
     * @return полная информация об отмененном событии
     */
    @PatchMapping("/{eventId}")
    public EventFullDto cancelEventByUser(@PathVariable int userId,
                                          @PathVariable int eventId) {
        return eventService.cancelEventByUser(userId, eventId);
    }
}
