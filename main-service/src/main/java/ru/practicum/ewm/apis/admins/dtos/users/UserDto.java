package ru.practicum.ewm.apis.admins.dtos.users;

import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@Validated
public class UserDto extends UserShortDto {
    @NotNull(message = "должно быть заполнено")
    @NotBlank(message = "не должно быть пустым или состоять из пробелов")
    @Email(message = "должно быть в формате email")
    @Size(max = 100, message = "не должно содержать больше 100 символов")
    private String email;

    public UserDto(int id, String name, String rating, String email) {
        super(id, name, rating);
        this.email = email;
    }
}
