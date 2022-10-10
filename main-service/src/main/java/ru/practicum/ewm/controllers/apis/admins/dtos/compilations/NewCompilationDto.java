package ru.practicum.ewm.controllers.apis.admins.dtos.compilations;

import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * Класс Dto, наследующий от {@link CommonCompilationDto} и описывающий основные свойства новой подборки событий,
 * созданной администратором
 *
 * @since 1.0
 */
@Getter
@Setter
@Validated
public class NewCompilationDto extends CommonCompilationDto {
    /**
     * Список идентификаторов событий, входящих в подборку
     *
     * @see ru.practicum.ewm.models.Event
     * @since 1.0
     */
    @NotNull(message = "не должно равняться null")
    private Set<Integer> events;

    public NewCompilationDto(Set<Integer> events, boolean pinned, String title) {
        super(pinned, title);
        this.events = events;
    }
}
