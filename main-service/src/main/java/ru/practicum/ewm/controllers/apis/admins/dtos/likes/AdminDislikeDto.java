package ru.practicum.ewm.controllers.apis.admins.dtos.likes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.ewm.controllers.apis.admins.dtos.users.UserDto;
import ru.practicum.ewm.models.dtos.events.EventFullDto;

/**
 * Класс, описывающий Dto сущность Like, для администратора
 *
 * @since 1.0
 */
@Getter
@Setter
@AllArgsConstructor
public class AdminDislikeDto {
    /**
     * Идентификатор дизлайка
     *
     * @since 1.0
     */
    private long id;
    /**
     * Пользователь, поставивший дизлайк
     *
     * @since 1.0
     */
    private UserDto user;
    /**
     * Событие, которому поставили дизлайк
     *
     * @since 1.0
     */
    private EventFullDto event;
}
