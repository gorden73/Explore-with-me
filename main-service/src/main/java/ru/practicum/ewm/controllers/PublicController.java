package ru.practicum.ewm.controllers;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.models.dto.categories.CategoryDto;
import ru.practicum.ewm.models.dto.complications.ComplicationDto;
import ru.practicum.ewm.models.dto.events.EventFullDto;
import ru.practicum.ewm.models.dto.events.EventShortDto;
import ru.practicum.ewm.services.CategoryService;
import ru.practicum.ewm.services.ComplicationService;
import ru.practicum.ewm.services.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.*;
import java.util.Collection;

@RestController
@Validated
public class PublicController {
    private final CategoryService categoryService;
    private final EventService eventService;
    private final ComplicationService complicationService;

    @Autowired
    public PublicController(CategoryService categoryService, EventService eventService,
                            ComplicationService complicationService) {
        this.categoryService = categoryService;
        this.eventService = eventService;
        this.complicationService = complicationService;
    }

    @GetMapping("/categories")
    public Collection<CategoryDto> getAllCategories(@RequestParam(defaultValue = "0")
                                                    @PositiveOrZero(message = "может быть равно или больше 0")
                                                    int from,
                                                    @RequestParam(defaultValue = "10")
                                                    @Positive(message = "может быть только больше 0")
                                                    int size) {
        return categoryService.getAllCategories(from, size);
    }

    @GetMapping("/categories/{catId}")
    public CategoryDto getCategoryById(@PathVariable(name = "catId") int id) {
        return categoryService.getCategoryDtoById(id);
    }

    @GetMapping("/events")
    public Collection<EventShortDto> getAllEvents(@RequestParam
                                                  @NotNull(message = "Запрос для поиска равен null.")
                                                  @NotBlank(message = "Пустой запрос для поиска.") String text,
                                                  @RequestParam
                                                  @NotNull(message = "список категорий для поиска равен null")
                                                  @NotEmpty(message = "список категорий для поиска пустой")
                                                  Integer[] categories,
                                                  @RequestParam Boolean paid,
                                                  @RequestParam(required = false)
                                                  @JsonFormat(shape = JsonFormat.Shape.STRING,
                                                          pattern = "yyyy-MM-dd HH:mm:ss") String rangeStart,
                                                  @RequestParam(required = false)
                                                  @JsonFormat(shape = JsonFormat.Shape.STRING,
                                                          pattern = "yyyy-MM-dd HH:mm:ss") String rangeEnd,
                                                  @RequestParam Boolean onlyAvailable,
                                                  @RequestParam String sort,
                                                  @RequestParam(defaultValue = "0")
                                                  @PositiveOrZero(message = "может быть равно или больше 0") int from,
                                                  @RequestParam(defaultValue = "10")
                                                  @Positive(message = "может быть только больше 0") int size,
                                                  HttpServletRequest request) {
        return eventService.getAllEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from,
                size, request);
        // информацию о том, что по этому эндпоинту был осуществлен и
        // обработан запрос, нужно сохранить в сервисе статистики
    }

    @GetMapping("/events/{id}")
    public EventFullDto getEventById(@PathVariable @NotNull @Positive int id, HttpServletRequest request) {
        return eventService.getFullEventById(id, request);
        // информацию о том, что по этому эндпоинту был осуществлен и
        // обработан запрос, нужно сохранить в сервисе статистики
    }

    @GetMapping("/complications")
    public Collection<ComplicationDto> getAllComplications(@RequestParam(required = false) Boolean pinned,
                                                           @RequestParam(defaultValue = "0")
                                                           @PositiveOrZero(message = "может быть равно или больше 0")
                                                           int from,
                                                           @RequestParam(defaultValue = "10")
                                                           @Positive(message = "может быть только больше 0") int size) {
        return complicationService.getAllComplications(pinned, from, size);
    }

    @GetMapping("/complications/{compId}")
    public ComplicationDto getComplicationById(@PathVariable int compId) {
        return complicationService.getComplicationDtoById(compId);
    }
}
