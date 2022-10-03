package ru.practicum.ewm.controllers.apis.nonauthorizedusers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.models.dtos.events.EventFullDto;
import ru.practicum.ewm.models.dtos.events.EventShortDto;
import ru.practicum.ewm.services.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.*;
import java.util.Collection;

/**
 * Класс для работы неавторизованного пользователя с событиями
 *
 * @since 1.0
 */
@RestController
@RequestMapping(path = "/events")
public class NonAuthorizedUserEventController {
    /**
     * Сервис для работы с событиями
     *
     * @since 1.0
     */
    private final EventService eventService;

    @Autowired
    public NonAuthorizedUserEventController(EventService eventService) {
        this.eventService = eventService;
    }

    /**
     * Метод позволяет получить коллекцию Dto с кратким описанием событий, подходящих под заданные условия
     *
     * @param text          текст для поиска в содержимом аннотации и подробном описании события
     * @param categories    список идентификаторов категорий, в которых будет вестись поиск
     * @param paid          поиск только платных/бесплатных событий
     * @param rangeStart    дата и время, не раньше которых должно произойти событие
     * @param rangeEnd      дата и время, не позже которых должно произойти событие
     * @param onlyAvailable дата и время не позже которых должно произойти событие (по умолчанию false)
     * @param sort          Вариант сортировки: по дате события (EVENT_DATE) или по количеству просмотров (VIEWS)
     * @param from          количество событий, которые нужно пропустить для формирования текущего набора (по умолчанию 0)
     * @param size          количество событий в наборе (по умолчанию 10)
     * @return коллекция Dto с кратким описанием событий
     * @since 1.0
     */
    @GetMapping
    public Collection<EventShortDto> getAllEvents(@RequestParam
                                                  @NotNull(message = "Запрос для поиска равен null.")
                                                  @NotBlank(message = "Пустой запрос для поиска.") String text,
                                                  @RequestParam
                                                  @NotNull(message = "список категорий для поиска равен null")
                                                  @NotEmpty(message = "список категорий для поиска пустой")
                                                  Integer[] categories,
                                                  @RequestParam Boolean paid,
                                                  @RequestParam(required = false)
                                                  @DateTimeFormat(pattern = "yyyy.MM.dd HH:mm:ss")
                                                  String rangeStart,
                                                  @RequestParam(required = false)
                                                  @DateTimeFormat(pattern = "yyyy.MM.dd HH:mm:ss")
                                                  String rangeEnd,
                                                  @RequestParam Boolean onlyAvailable,
                                                  @RequestParam String sort,
                                                  @RequestParam(defaultValue = "0")
                                                  @PositiveOrZero(message = "может быть равно или больше 0") int from,
                                                  @RequestParam(defaultValue = "10")
                                                  @Positive(message = "может быть только больше 0") int size,
                                                  HttpServletRequest request) {
        return eventService.getAllEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from,
                size, request);
    }

    /**
     * Метод позволяет получить Dto события с подробной информацией о нем по идентификатору
     *
     * @param id идентификатор события
     * @return Dto события с подробной информацией о нем
     * @since 1.0
     */
    @GetMapping("/{id}")
    public EventFullDto getEventById(@PathVariable @NotNull @Positive int id, HttpServletRequest request) {
        return eventService.getFullEventById(id, request);
    }
}
