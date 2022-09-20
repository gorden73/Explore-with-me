package ru.practicum.ewm.models.dto.compilations;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@Validated
public class NewCompilationDto {
    @NotNull(message = "не должно равняться null")
    //@NotEmpty(message = "не должно быть пустым")
    private Set<Integer> events;
    private boolean pinned;
    @NotNull(message = "не должно равняться null")
    @Size(min = 3, max = 120, message = "должно содержать от 20 до 2000 символов")
    private String title;
}
