package ru.practicum.ewm.controllers.apis.authorizedusers.dtos.likes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Класс, описывающий Dto сущность Like, для авторизованного пользователя
 *
 * @since 1.0
 */
@Getter
@Setter
@AllArgsConstructor
public class LikeDto {
    /**
     * Имя пользователя, поставившего лайк
     *
     * @since 1.0
     */
    private String userName;
}
