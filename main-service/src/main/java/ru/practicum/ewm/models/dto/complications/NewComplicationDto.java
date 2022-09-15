package ru.practicum.ewm.models.dto.complications;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@Validated
public class NewComplicationDto {
    @NotNull
    @NotEmpty
    private Set<Integer> events;
    private boolean pinned;
    @NotNull
    @Min(3)
    @Max(120)
    private String title;
}
