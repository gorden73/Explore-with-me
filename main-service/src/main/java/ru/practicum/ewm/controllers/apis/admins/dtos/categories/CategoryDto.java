package ru.practicum.ewm.controllers.apis.admins.dtos.categories;

import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Класс Dto, описывающий свойства категории событий
 * @since 1.0
 */
@Getter
@Setter
@Validated
public class CategoryDto {
    /**
     * Идентификатор категории
     * @since 1.0
     */
    private int id;
    /**
     * Название категории
     * @since 1.0
     */
    @NotNull(message = "должно быть заполнено")
    @Size(min = 3, max = 50, message = "должно содержать от 3 до 50 символов")
    private String name;

    public CategoryDto(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
