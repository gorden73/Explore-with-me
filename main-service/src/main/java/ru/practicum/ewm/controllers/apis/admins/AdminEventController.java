package ru.practicum.ewm.controllers.apis.admins;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.controllers.apis.admins.dtos.events.AdminUpdateEventRequestDto;
import ru.practicum.ewm.models.dtos.events.EventFullDto;
import ru.practicum.ewm.services.EventService;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;

/**
 * Контроллер для работы администратора с событиями
 *
 * @since 1.0
 */
@RestController
@RequestMapping(path = "/admin/events")
public class AdminEventController {
    /**
     * Сервис для работы с событиями
     *
     * @see EventService
     * @since 1.0
     */
    private final EventService eventService;

    @Autowired
    public AdminEventController(EventService eventService) {
        this.eventService = eventService;
    }

    /**
     * Метод позволяет найти события, подходящие под переданные условия
     *
     * @param users      список идентификаторов пользователей, события которых нужно найти
     * @param states     список статусов, в которых находятся искомые события
     * @param categories список идентификаторов категорий, в которых нужно вести поиск
     * @param rangeStart дата и время, не раньше которых должно произойти событие
     * @param rangeEnd   дата и время, не позже которых должно произойти событие
     * @param from       количество событий, которые нужно пропустить для формирования текущего набора(по умолчанию 0)
     * @param size       количество событий в наборе(по умолчанию 10)
     * @return полная информация обо всех событиях подходящих под переданные условия
     * @since 1.0
     */
    @GetMapping
    public Collection<EventFullDto> searchEvents(@RequestParam @NotEmpty(message = "не должно быть пустым")
                                                 Integer[] users,
                                                 @RequestParam @NotEmpty(message = "не должно быть пустым")
                                                 String[] states,
                                                 @RequestParam @NotEmpty(message = "не должно быть пустым")
                                                 Integer[] categories,
                                                 @RequestParam
                                                 @DateTimeFormat(pattern = "yyyy.MM.dd HH:mm:ss")
                                                 String rangeStart,
                                                 @RequestParam
                                                 @DateTimeFormat(pattern = "yyyy.MM.dd HH:mm:ss")
                                                 String rangeEnd,
                                                 @RequestParam
                                                 @PositiveOrZero(message = "может быть равно или больше 0")
                                                 int from,
                                                 @RequestParam
                                                 @Positive(message = "может быть только больше 0")
                                                 int size) {
        return eventService.searchEventsToAdmin(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    /**
     * Метод позволяет обновить событие по идентификатору
     *
     * @param eventId  идентификатор события
     * @param eventDto объект, описывающий свойства для обновления события, которые задает администратор
     * @return полная информация об обновленном объекте
     * @since 1.0
     */
    @PutMapping("/{eventId}")
    public EventFullDto updateEvent(@PathVariable int eventId,
                                    @RequestBody AdminUpdateEventRequestDto eventDto) {
        return eventService.updateEventByAdmin(eventId, eventDto);
    }

    /**
     * Метод позволяет опубликовать событие, добавленное пользователей, по идентификатору и находящееся в состоянии
     * ожидания модерации
     *
     * @param eventId идентификатор события
     * @return полная информация об опубликованном событии
     * @since 1.0
     */
    @PatchMapping("/{eventId}/publish")
    public EventFullDto publishEvent(@PathVariable int eventId) {
        return eventService.publishEvent(eventId);
    }

    /**
     * Метод позволяет отклонить публикацию события, добавленного пользователем, по идентификатору
     *
     * @param eventId идентификатор события
     * @return полная информация об отклоненном событии
     * @since 1.0
     */
    @PatchMapping("/{eventId}/reject")
    public EventFullDto rejectEvent(@PathVariable int eventId) {
        return eventService.rejectEvent(eventId);
    }
}
