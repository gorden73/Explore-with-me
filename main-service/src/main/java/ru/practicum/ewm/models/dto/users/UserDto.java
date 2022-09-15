package ru.practicum.ewm.models.dto.users;

import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.*;

@Getter
@Setter
@Validated
public class UserDto extends UserShortDto {
    @NotNull(message = "должно быть заполнено")
    @NotBlank(message = "не должно быть пустым или состоять из пробелов")
    @Email(message = "должно быть в формате email")
    @Size(max = 100, message = "не должно содержать больше 100 символов")
    private String email;

    public UserDto(int id, String name, String email) {
        super(id, name);
        this.email = email;
    }
}
