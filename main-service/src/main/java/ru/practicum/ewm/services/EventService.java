package ru.practicum.ewm.services;

import ru.practicum.ewm.models.Event;
import ru.practicum.ewm.models.dto.events.*;
import ru.practicum.ewm.models.dto.likes.AdminLikeDto;
import ru.practicum.ewm.models.dto.likes.LikeDto;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;

public interface EventService {
    Collection<EventShortDto> getAllEvents(String text, Integer[] categories, Boolean paid, String rangeStart,
                                           String rangeEnd, boolean onlyAvailable, String sort, int from, int size,
                                           HttpServletRequest request);

    EventFullDto getFullEventById(int id, HttpServletRequest request);

    Event getEventById(int id);

    EventFullDto addEvent(int userId, NewEventDto eventDto);

    Collection<EventFullDto> searchEventsToAdmin(Integer[] users, String[] states, Integer[] categories,
                                                 String rangeStart, String rangeEnd, int from, int size);

    EventFullDto updateEventByAdmin(int eventId, AdminUpdateEventRequestDto eventDto);

    EventFullDto publishEvent(int eventId);

    EventFullDto rejectEvent(int eventId);

    Collection<EventShortDto> getUserEvents(int userId, int from, int size);

    EventFullDto updateUserEvent(int userId, UpdateEventRequestDto eventDto);

    EventFullDto getUserEvent(int userId, int eventId);

    EventFullDto cancelEventByUser(int userId, int eventId);

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
}
