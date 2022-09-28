package ru.practicum.ewm.apis.authorizedusers.dtos.events;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@Validated
public class EventDto {
    @Size(min = 20, max = 2000, message = "должно содержать от 20 до 2000 символов")
    private String annotation;
    @NotNull(message = "не должно быть равно null")
    private Integer category;
    @Size(min = 20, max = 7000, message = "должно содержать от 20 до 7000 символов")
    private String description;
    @NotNull(message = "не должно быть равно null")
    @NotBlank(message = "не должно быть пустым или состоять из пробелов")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private String eventDate;
    private Boolean paid;
    private Integer participantLimit;
    @Size(min = 3, max = 120, message = "должно содержать от 3 до 120 символов")
    private String title;
}
