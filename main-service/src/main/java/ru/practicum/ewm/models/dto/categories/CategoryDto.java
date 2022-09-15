package ru.practicum.ewm.models.dto.categories;

import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@Validated
public class CategoryDto {
    private int id;
    @NotNull(message = "должно быть заполнено")
    @Size(min = 3, max = 50, message = "должно содержать от 3 до 50 символов")
    private String name;

    public CategoryDto(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
