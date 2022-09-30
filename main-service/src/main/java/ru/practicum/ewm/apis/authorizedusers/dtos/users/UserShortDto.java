package ru.practicum.ewm.apis.authorizedusers.dtos.users;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Родительский класс Dto, описывающий краткую информацию о пользователе
 *
 * @since 1.0
 */
@Getter
@Setter
@EqualsAndHashCode
@Validated
public class UserShortDto {
    /**
     * Идентификатор пользователя
     *
     * @since 1.0
     */
    private int id;
    /**
     * Имя пользователя
     *
     * @since 1.0
     */
    @NotNull(message = "должно быть заполнено")
    @Size(min = 3, max = 50, message = "должно содержать от 3 до 50 символов")
    private String name;
    private String rating;

    public UserShortDto(int id, String name, String rating) {
        this.id = id;
        this.name = name;
        this.rating = rating;
    }
}
