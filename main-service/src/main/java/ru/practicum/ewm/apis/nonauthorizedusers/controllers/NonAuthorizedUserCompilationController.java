package ru.practicum.ewm.apis.nonauthorizedusers.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.models.dtos.compilations.CompilationDto;
import ru.practicum.ewm.services.CompilationService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;

/**
 * Класс для работы неавторизованного пользователя с подборками событий
 *
 * @since 1.0
 */
@RestController
@RequestMapping(path = "/compilations")
public class NonAuthorizedUserCompilationController {
    /**
     * Сервис для работы с подборками событий
     *
     * @since 1.0
     */
    private final CompilationService compilationService;

    @Autowired
    public NonAuthorizedUserCompilationController(CompilationService compilationService) {
        this.compilationService = compilationService;
    }

    /**
     * Метод позволяет получить коллекцию Dto подборок событий, подходящих под заданные условия
     *
     * @param pinned закреплена ли подборка на главной странце
     * @param from   количество подборок, которое надо пропустить для формирования коллекции
     * @param size   количество подборок в коллекции
     * @return коллекция Dto подборок событий
     * @since 1.0
     */
    @GetMapping
    public Collection<CompilationDto> getAllCompilations(@RequestParam(required = false) Boolean pinned,
                                                         @RequestParam(defaultValue = "0")
                                                         @PositiveOrZero(message = "может быть равно или больше 0")
                                                         int from,
                                                         @RequestParam(defaultValue = "10")
                                                         @Positive(message = "может быть только больше 0") int size) {
        return compilationService.getAllCompilations(pinned, from, size);
    }

    /**
     * Метод позволяет получить Dto подборки событий по идентификатору
     *
     * @param compId идентификатор подборки
     * @return Dto подборки событий
     * @since 1.0
     */
    @GetMapping("/{compId}")
    public CompilationDto getCompilationById(@PathVariable int compId) {
        return compilationService.getCompilationDtoById(compId);
    }
}
