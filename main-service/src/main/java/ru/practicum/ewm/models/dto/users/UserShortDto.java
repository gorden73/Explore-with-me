package ru.practicum.ewm.models.dto.users;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.*;

@Getter
@Setter
@EqualsAndHashCode
@Validated
public class UserShortDto {
    private int id;
    @NotNull(message = "должно быть заполнено")
    @Size(min = 3, max = 50, message = "должно содержать от 3 до 50 символов")
    private String name;

    public UserShortDto(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
