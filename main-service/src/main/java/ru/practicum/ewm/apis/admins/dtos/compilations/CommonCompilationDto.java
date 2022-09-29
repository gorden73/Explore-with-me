package ru.practicum.ewm.apis.admins.dtos.compilations;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Родительский класс Dto, описывающий основные свойства подборки событий
 * данными
 * @since 1.0
 */
@Getter
@Setter
@AllArgsConstructor
@Validated
public class CommonCompilationDto {
    /**
     * Закреплена ли подборка на главной странице
     * @since 1.0
     */
    private boolean pinned;
    /**
     * Заголовок подборки
     * @since 1.0
     */
    @NotNull(message = "не должно равняться null")
    @Size(min = 3, max = 120, message = "должно содержать от 20 до 2000 символов")
    private String title;
}
