package ru.practicum.ewm.models.dto.likes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Класс, описывающий Dto сущность Like, для авторизованного пользователя
 */
@Getter
@Setter
@AllArgsConstructor
public class LikeDto {
    /**
     * Имя пользователя, поставившего лайк/дизлайк
     */
    private String userName;
}
