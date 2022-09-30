package ru.practicum.ewm.apis.nonauthorizedusers.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.models.dtos.compilations.CompilationDto;
import ru.practicum.ewm.services.CompilationService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;

/**
 *
 */
@RestController
@RequestMapping(path = "/compilations")
public class NonAuthorizedUserCompilationController {
    private final CompilationService compilationService;

    @Autowired
    public NonAuthorizedUserCompilationController(CompilationService compilationService) {
        this.compilationService = compilationService;
    }

    @GetMapping
    public Collection<CompilationDto> getAllCompilations(@RequestParam(required = false) Boolean pinned,
                                                         @RequestParam(defaultValue = "0")
                                                         @PositiveOrZero(message = "может быть равно или больше 0")
                                                         int from,
                                                         @RequestParam(defaultValue = "10")
                                                         @Positive(message = "может быть только больше 0") int size) {
        return compilationService.getAllCompilations(pinned, from, size);
    }

    @GetMapping("/{compId}")
    public CompilationDto getCompilationById(@PathVariable int compId) {
        return compilationService.getCompilationDtoById(compId);
    }
}
