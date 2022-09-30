package ru.practicum.ewm.services;

import ru.practicum.ewm.models.Event;
import ru.practicum.ewm.models.dto.events.EventShortDto;
import ru.practicum.ewm.models.dto.likes.AdminLikeDto;
import ru.practicum.ewm.models.dto.likes.LikeDto;

import java.util.List;

public interface LikeService {
    /**
     * Метод позволяет пользователю поставить лайк чужому событию
     *
     * @param userId  идентификатор пользователя
     * @param eventId идентификатор события
     * @return краткая информация о событии, которому пользователь поставил лайк
     */
    EventShortDto addLike(int userId, int eventId);

    /**
     * Метод позволяет получить краткую информацию по всем лайкам указанного события
     *
     * @param userId  идентификатор пользователя
     * @param eventId идентификатор события
     * @return краткая информация по всем лайкам указанного события
     */
    List<LikeDto> getEventLikesDto(Integer userId, int eventId);

    /**
     * Метод позволяет получить подробную информацию по всем лайкам указанного события
     *
     * @param userId  идентификатор пользователя
     * @param eventId идентификатор события
     * @return подробная информация по всем лайкам указанного события
     */
    List<AdminLikeDto> getEventAdminLikesDto(Integer userId, int eventId);

    /**
     * Метод позволяет пользователю поставить дизлайк чужому событию
     *
     * @param userId  идентификатор пользователя
     * @param eventId идентификатор события
     * @return краткая информация о событии, которому пользователь поставил дизлайк
     */
    EventShortDto addDislike(int userId, int eventId);

    /**
     * Метод позволяет получить краткую информацию по всем дизлайкам указанного события
     *
     * @param userId  идентификатор пользователя
     * @param eventId идентификатор события
     * @return краткая информация по всем дизлайкам указанного события
     */
    List<LikeDto> getEventDislikesDto(Integer userId, int eventId);

    /**
     * Метод позволяет получить подробную информацию по всем дизлайкам указанного события
     *
     * @param userId  идентификатор пользователя
     * @param eventId идентификатор события
     * @return подробная информация по всем дизлайкам указанного события
     */
    List<AdminLikeDto> getEventAdminDislikesDto(Integer userId, int eventId);

    /**
     * Метод позволяет посчитать количество лайков и дизлайков события
     *
     * @param event  событие для расчёта
     * @param userId идентификатор пользователя (организатор события)
     */
    void calculateEventLikesAndDislikes(Event event, int userId);
}
