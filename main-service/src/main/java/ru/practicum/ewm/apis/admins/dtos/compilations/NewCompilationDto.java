package ru.practicum.ewm.apis.admins.dtos.compilations;

import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Getter
@Setter
@Validated
public class NewCompilationDto extends CommonCompilationDto {
    @NotNull(message = "не должно равняться null")
    private Set<Integer> events;

    public NewCompilationDto(Set<Integer> events, boolean pinned, String title) {
        super(pinned, title);
        this.events = events;
    }
}
