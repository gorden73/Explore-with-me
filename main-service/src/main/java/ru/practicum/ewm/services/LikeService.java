package ru.practicum.ewm.services;

import ru.practicum.ewm.controllers.apis.admins.dtos.likes.AdminDislikeDto;
import ru.practicum.ewm.controllers.apis.admins.dtos.likes.AdminLikeDto;
import ru.practicum.ewm.controllers.apis.authorizedusers.dtos.likes.DislikeDto;
import ru.practicum.ewm.controllers.apis.authorizedusers.dtos.likes.LikeDto;
import ru.practicum.ewm.models.dtos.events.EventShortDto;

import java.util.List;

/**
 * Сервис для работы с лайками/дизлайками событий
 *
 * @see EventShortDto
 * @see LikeDto
 * @see DislikeDto
 * @see AdminLikeDto
 * @see AdminDislikeDto
 * @since 1.1
 */
public interface LikeService {
    /**
     * Метод позволяет пользователю поставить лайк чужому событию
     *
     * @param userId  идентификатор пользователя
     * @param eventId идентификатор события
     * @return краткая информация о событии, которому пользователь поставил лайк
     * @since 1.1
     */
    EventShortDto addLike(int userId, int eventId);

    /**
     * Метод позволяет получить краткую информацию по всем лайкам указанного события
     *
     * @param userId  идентификатор пользователя
     * @param eventId идентификатор события
     * @return краткая информация по всем лайкам указанного события
     * @since 1.1
     */
    List<LikeDto> getEventLikesDto(Integer userId, int eventId, int from, int size);

    /**
     * Метод позволяет получить подробную информацию по всем лайкам указанного события
     *
     * @param eventId идентификатор события
     * @return подробная информация по всем лайкам указанного события
     * @since 1.1
     */
    List<AdminLikeDto> getEventAdminLikesDto(int eventId, int from, int size);

    /**
     * Метод позволяет пользователю поставить дизлайк чужому событию
     *
     * @param userId  идентификатор пользователя
     * @param eventId идентификатор события
     * @return краткая информация о событии, которому пользователь поставил дизлайк
     * @since 1.1
     */
    EventShortDto addDislike(int userId, int eventId);

    /**
     * Метод позволяет получить краткую информацию по всем дизлайкам указанного события
     *
     * @param userId  идентификатор пользователя
     * @param eventId идентификатор события
     * @return краткая информация по всем дизлайкам указанного события
     * @since 1.1
     */
    List<DislikeDto> getEventDislikesDto(Integer userId, int eventId, int from, int size);

    /**
     * Метод позволяет получить подробную информацию по всем дизлайкам указанного события
     *
     * @param eventId идентификатор события
     * @return подробная информация по всем дизлайкам указанного события
     * @since 1.1
     */
    List<AdminDislikeDto> getEventAdminDislikesDto(int eventId, int from, int size);
}
