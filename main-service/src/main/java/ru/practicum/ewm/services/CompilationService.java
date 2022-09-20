package ru.practicum.ewm.services;

import ru.practicum.ewm.models.Compilation;
import ru.practicum.ewm.models.dto.compilations.CompilationDto;
import ru.practicum.ewm.models.dto.compilations.NewCompilationDto;

import java.util.Collection;

public interface CompilationService {
    Collection<CompilationDto> getAllCompilations(Boolean pinned, int from, int size);

    CompilationDto getCompilationDtoById(int id);

    Compilation getCompilationById(int id);

    CompilationDto addCompilation(NewCompilationDto compilationDto);

    void removeCompilationById(int id);

    void removeEventFromCompilation(int compId, int eventId);

    void addEventToCompilation(int compId, int eventId);

    void unpinCompilationAtMainPage(int compId);

    void pinCompilationAtMainPage(int compId);
}
