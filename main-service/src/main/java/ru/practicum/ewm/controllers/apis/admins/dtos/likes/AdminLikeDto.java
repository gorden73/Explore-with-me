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
public class AdminLikeDto {
    /**
     * Идентификатор лайка
     *
     * @since 1.0
     */
    private long id;
    /**
     * Пользователь, поставивший лайк
     *
     * @since 1.0
     */
    private UserDto user;
    /**
     * Событие, которому поставили лайк
     *
     * @since 1.0
     */
    private EventFullDto event;
}