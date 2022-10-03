package ru.practicum.ewm.models.dtos.compilations;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.ewm.controllers.apis.admins.dtos.compilations.CommonCompilationDto;
import ru.practicum.ewm.models.dtos.events.EventShortDto;

import java.util.Set;

/**
 * Класс, описывающий Dto подборки событий
 *
 * @see CommonCompilationDto
 * @since 1.0
 */
@Getter
@Setter
public class CompilationDto extends CommonCompilationDto {
    /**
     * Идентификатор подборки событий
     *
     * @since 1.0
     */
    private int id;
    /**
     * Список Dto событий с краткой информацией о них
     *
     * @since 1.0
     */
    private Set<EventShortDto> events;

    public CompilationDto(Set<EventShortDto> events, int id, boolean pinned, String title) {
        super(pinned, title);
        this.id = id;
        this.events = events;
    }
}
