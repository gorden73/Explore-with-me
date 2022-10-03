package ru.practicum.ewm.controllers.apis.authorizedusers.dtos.events;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Родительский класс Dto, описывающий основные свойства события {@link ru.practicum.ewm.models.Event}
 * @since 1.0
 */
@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@Validated
public class EventDto {
    /**
     * Краткое описание события
     * @since 1.0
     */
    @Size(min = 20, max = 2000, message = "должно содержать от 20 до 2000 символов")
    private String annotation;
    /**
     * Категория события
     * @since 1.0
     */
    @NotNull(message = "не должно быть равно null")
    private Integer category;
    /**
     * Подробное описание события
     * @since 1.0
     */
    @Size(min = 20, max = 7000, message = "должно содержать от 20 до 7000 символов")
    private String description;
    /**
     * Дата и время на которые намечено событие (в формате "yyyy-MM-dd HH:mm:ss")
     * @since 1.0
     */
    @NotNull(message = "не должно быть равно null")
    @NotBlank(message = "не должно быть пустым или состоять из пробелов")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private String eventDate;
    /**
     * Нужно ли оплачивать участие(по умолчанию false)
     * @since 1.0
     */
    private Boolean paid;
    /**
     * Ограничение на количество участников. Значение 0 - означает отсутствие ограничения. По умолчанию 0.
     * @since 1.0
     */
    private Integer participantLimit;
    /**
     * Заголовок события
     * @since 1.0
     */
    @Size(min = 3, max = 120, message = "должно содержать от 3 до 120 символов")
    private String title;
}
