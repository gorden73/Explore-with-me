package ru.practicum.ewm.controllers.apis.admins.dtos.mappers;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.controllers.apis.admins.dtos.compilations.NewCompilationDto;
import ru.practicum.ewm.controllers.apis.authorizedusers.dtos.mappers.EventMapper;
import ru.practicum.ewm.models.Compilation;
import ru.practicum.ewm.models.dtos.compilations.CompilationDto;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Класс для работы с подборкой событий и преобразования Dto в сущность и обратно
 *
 * @see Compilation
 * @since 1.0
 */
@Component
public class CompilationMapper {
    /**
     * Метод позволяет преобразовать сущность подборки событий в Dto
     *
     * @param compilation сущность подборки событий
     * @return Dto подборки событий
     * @since 1.0
     */
    public static CompilationDto toDto(Compilation compilation) {
        return new CompilationDto(
                EventMapper.toEventDtoCollection(compilation.getEvents()),
                compilation.getId(),
                compilation.isPinned(),
                compilation.getTitle());
    }

    /**
     * Метод позволяет преобразовать коллекцию подборок событий в коллекцию Dto событий
     *
     * @param compilations коллекция подборок событий
     * @return коллекция Dto событий
     * @since 1.0
     */
    public static Collection<CompilationDto> toDtoCollection(Collection<Compilation> compilations) {
        return compilations.stream()
                .map(CompilationMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Метод позволяет преобразовать Dto новой подборки в сущность подборки событий
     *
     * @param dto Dto новой подборки
     * @return сущность подборки событий
     * @since 1.0
     */
    public static Compilation toCompilation(NewCompilationDto dto) {
        return new Compilation(
                dto.isPinned(),
                dto.getTitle()
        );
    }
}
