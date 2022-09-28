package ru.practicum.ewm.apis.admins.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.models.dtos.compilations.CompilationDto;
import ru.practicum.ewm.apis.admins.dtos.compilations.NewCompilationDto;
import ru.practicum.ewm.services.CompilationService;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/admin/compilations")
@Validated
public class AdminCompilationController {
    private final CompilationService compilationService;

    @Autowired
    public AdminCompilationController(CompilationService compilationService) {
        this.compilationService = compilationService;
    }

    @PostMapping
    public CompilationDto addCompilation(@Valid @RequestBody NewCompilationDto compilationDto) {
        return compilationService.addCompilation(compilationDto);
    }

    @DeleteMapping("/{compId}")
    public void removeCompilationById(@PathVariable int compId) {
        compilationService.removeCompilationById(compId);
    }

    @DeleteMapping("/{compId}/events/{eventId}")
    public void removeEventFromCompilation(@PathVariable int compId,
                                           @PathVariable int eventId) {
        compilationService.removeEventFromCompilation(compId, eventId);
    }

    @PatchMapping("/{compId}/events/{eventId}")
    public void addEventToCompilation(@PathVariable int compId,
                                      @PathVariable int eventId) {
        compilationService.addEventToCompilation(compId, eventId);
    }

    @DeleteMapping("/{compId}/pin")
    public void unpinCompilationAtMainPage(@PathVariable int compId) {
        compilationService.unpinCompilationAtMainPage(compId);
    }

    @PatchMapping("/{compId}/pin")
    public void pinCompilationAtMainPage(@PathVariable int compId) {
        compilationService.pinCompilationAtMainPage(compId);
    }
}
