package ru.practicum.ewm.apis.authorizedusers.dtos.users;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Класс Dto, описывающий основные свойства нового пользователя, которые задает администратор
 *
 * @since 1.0
 */
@Getter
@Setter
@AllArgsConstructor
@Validated
public class NewUserRequest {
    /**
     * Электронный адрес пользователя
     *
     * @since 1.0
     */
    @NotNull(message = "должно быть заполнено")
    @NotBlank(message = "не должно быть пустым или состоять из пробелов")
    @Email(message = "должно быть в формате email")
    @Size(max = 100, message = "не должно содержать больше 100 символов")
    private String email;
    /**
     * Имя пользователя
     *
     * @since 1.0
     */
    @NotNull(message = "должно быть заполнено")
    @Size(min = 3, max = 50, message = "должно содержать от 3 до 50 символов")
    private String name;
}
