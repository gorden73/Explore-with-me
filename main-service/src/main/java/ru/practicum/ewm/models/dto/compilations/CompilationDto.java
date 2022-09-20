package ru.practicum.ewm.models.dto.compilations;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class CompilationDto extends NewCompilationDto {
    private int id;

    public CompilationDto(Set<Integer> events, int id, boolean pinned, String title) {
        super(events, pinned, title);
        this.id = id;
    }
}
