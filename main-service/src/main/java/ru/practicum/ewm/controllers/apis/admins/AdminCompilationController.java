package ru.practicum.ewm.controllers.apis.admins;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.controllers.apis.admins.dtos.compilations.NewCompilationDto;
import ru.practicum.ewm.models.dtos.compilations.CompilationDto;
import ru.practicum.ewm.services.CompilationService;

import javax.validation.Valid;

/**
 * Контроллер для работы администратора с подборками событий
 *
 * @since 1.0
 */
@RestController
@RequestMapping(path = "/admin/compilations")
@Validated
public class AdminCompilationController {
    /**
     * Сервис для работы с подборками событий
     *
     * @see CompilationService
     * @since 1.0
     */
    private final CompilationService compilationService;

    @Autowired
    public AdminCompilationController(CompilationService compilationService) {
        this.compilationService = compilationService;
    }

    /**
     * Метод позволяет создать новую подборку событий
     *
     * @param compilationDto объект, описывающий основые свойства подборки событий, которые задает администратор
     * @return созданный объект, описывающий основные и дополнительные свойства подборки событий
     * @since 1.0
     */
    @PostMapping
    public CompilationDto addCompilation(@Valid @RequestBody NewCompilationDto compilationDto) {
        return compilationService.addCompilation(compilationDto);
    }

    /**
     * Метод позволяет удалить имеющуюся подборку событий по идентификатору
     *
     * @param compId идентификатор подборки событий
     * @since 1.0
     */
    @DeleteMapping("/{compId}")
    public void removeCompilationById(@PathVariable int compId) {
        compilationService.removeCompilationById(compId);
    }

    /**
     * Метод позволяет удалить событие из подборки по идентификаторам подборки и события
     *
     * @param compId  идентификатор подборки событий
     * @param eventId идентификатор события
     * @since 1.0
     */
    @DeleteMapping("/{compId}/events/{eventId}")
    public void removeEventFromCompilation(@PathVariable int compId,
                                           @PathVariable int eventId) {
        compilationService.removeEventFromCompilation(compId, eventId);
    }

    /**
     * Метод позволяет добавить событие в подборку по идентификаторам подборки и события
     *
     * @param compId  идентификатор подборки событий
     * @param eventId идентификатор события
     * @since 1.0
     */
    @PatchMapping("/{compId}/events/{eventId}")
    public void addEventToCompilation(@PathVariable int compId,
                                      @PathVariable int eventId) {
        compilationService.addEventToCompilation(compId, eventId);
    }

    /**
     * Метод позволяет открепить подборку по идентификатору от главной страницы
     *
     * @param compId идентификатор подборки событий
     * @since 1.0
     */
    @DeleteMapping("/{compId}/pin")
    public void unpinCompilationAtMainPage(@PathVariable int compId) {
        compilationService.unpinCompilationAtMainPage(compId);
    }

    /**
     * Метод позволяет закрепить подборку по идентификатору на главной странице
     *
     * @param compId идентификатор подборки событий
     * @since 1.0
     */
    @PatchMapping("/{compId}/pin")
    public void pinCompilationAtMainPage(@PathVariable int compId) {
        compilationService.pinCompilationAtMainPage(compId);
    }
}
