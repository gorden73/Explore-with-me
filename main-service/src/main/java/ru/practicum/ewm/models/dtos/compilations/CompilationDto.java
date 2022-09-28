package ru.practicum.ewm.models.dtos.compilations;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.ewm.apis.admins.dtos.compilations.CommonCompilationDto;
import ru.practicum.ewm.models.dtos.events.EventShortDto;

import java.util.Set;

@Getter
@Setter
public class CompilationDto extends CommonCompilationDto {
    private int id;
    private Set<EventShortDto> events;

    public CompilationDto(Set<EventShortDto> events, int id, boolean pinned, String title) {
        super(pinned, title);
        this.id = id;
        this.events = events;
    }
}
