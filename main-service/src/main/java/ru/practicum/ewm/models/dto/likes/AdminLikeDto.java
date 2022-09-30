package ru.practicum.ewm.models.dto.likes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.ewm.models.dto.events.EventFullDto;
import ru.practicum.ewm.models.dto.users.UserDto;

/**
 * Класс, описывающий Dto сущность Like, для администратора
 */
@Getter
@Setter
@AllArgsConstructor
public class AdminLikeDto {
    /**
     * Идентификатор лайка/дизлайка
     */
    private int id;
    /**
     * Пользователь, поставивший лайк/дизлайк
     */
    private UserDto user;
    /**
     * Событие, которому поставили лайк/дизлайк
     */
    private EventFullDto event;
}
