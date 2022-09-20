package ru.practicum.ewm.models.dto.mappers;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.models.Compilation;
import ru.practicum.ewm.models.Event;
import ru.practicum.ewm.models.dto.compilations.CompilationDto;
import ru.practicum.ewm.models.dto.compilations.NewCompilationDto;

import java.util.Collection;
import java.util.stream.Collectors;

@Component
public class CompilationMapper {
    public static CompilationDto toDto(Compilation compilation) {
        return new CompilationDto(
                compilation.getEvents().stream().map(Event::getId).collect(Collectors.toSet()),
                compilation.getId(),
                compilation.isPinned(),
                compilation.getTitle());
    }

    public static Collection<CompilationDto> toDtoCollection(Collection<Compilation> compilations) {
        return compilations.stream()
                .map(CompilationMapper::toDto)
                .collect(Collectors.toList());
    }

    public static Compilation toCompilation(NewCompilationDto dto) {
        return new Compilation(
                dto.isPinned(),
                dto.getTitle()
        );
    }
}
