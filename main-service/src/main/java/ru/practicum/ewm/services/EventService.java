package ru.practicum.ewm.services;

import ru.practicum.ewm.apis.admins.dtos.events.AdminUpdateEventRequestDto;
import ru.practicum.ewm.apis.authorizedusers.dtos.events.NewEventDto;
import ru.practicum.ewm.apis.authorizedusers.dtos.events.UpdateEventRequestDto;
import ru.practicum.ewm.models.Event;
import ru.practicum.ewm.models.dtos.events.EventFullDto;
import ru.practicum.ewm.models.dtos.events.EventShortDto;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

/**
 * Интерфейс сервиса для работы с событиями
 *
 * @see Event
 * @see EventShortDto
 * @see EventFullDto
 * @since 1.0
 */
public interface EventService {
    /**
     * Метод позволяет получить коллекцию Dto с кратким описанием событий, подходящих под заданные условия
     *
     * @param text          текст для поиска в содержимом аннотации и подробном описании события
     * @param categories    список идентификаторов категорий, в которых будет вестись поиск
     * @param paid          поиск только платных/бесплатных событий
     * @param rangeStart    дата и время, не раньше которых должно произойти событие
     * @param rangeEnd      дата и время, не позже которых должно произойти событие
     * @param onlyAvailable дата и время не позже которых должно произойти событие (по умолчанию false)
     * @param sort          Вариант сортировки: по дате события (EVENT_DATE) или по количеству просмотров (VIEWS)
     * @param from          количество событий, которые нужно пропустить для формирования текущего набора (по умолчанию 0)
     * @param size          количество событий в наборе (по умолчанию 10)
     * @return коллекция Dto с кратким описанием событий
     * @since 1.0
     */
    Collection<EventShortDto> getAllEvents(String text, Integer[] categories, boolean paid, String rangeStart,
                                           String rangeEnd, boolean onlyAvailable, String sort, int from, int size,
                                           HttpServletRequest request);

    /**
     * Метод позволяет получить Dto события с подробной информацией о нем по идентификатору
     *
     * @param id идентификатор события
     * @return Dto события с подробной информацией о нем
     * @since 1.0
     */
    EventFullDto getFullEventById(int id, HttpServletRequest request);

    /**
     * Метод позволяет получить событие по идентификатору
     *
     * @param id идентификатор события
     * @return события
     * @since 1.0
     */
    Event getEventById(int id);

    /**
     * Метод позволяет пользователю создать новое событие
     *
     * @param userId   идентификатор пользователя {@link ru.practicum.ewm.models.User}
     * @param eventDto объект Dto, описывающий свойства для создания нового события {@link NewEventDto}
     * @return полная информация по новому событию
     * @since 1.0
     */
    EventFullDto addEvent(int userId, NewEventDto eventDto);

    /**
     * Метод позволяет найти события, подходящие под переданные условия
     *
     * @param users      список идентификаторов пользователей, события которых нужно найти
     * @param states     список статусов, в которых находятся искомые события
     * @param categories список идентификаторов категорий, в которых нужно вести поиск
     * @param rangeStart дата и время, не раньше которых должно произойти событие
     * @param rangeEnd   дата и время, не позже которых должно произойти событие
     * @param from       количество событий, которые нужно пропустить для формирования текущего набора(по умолчанию 0)
     * @param size       количество событий в наборе(по умолчанию 10)
     * @return полная информация обо всех событиях подходящих под переданные условия
     * @since 1.0
     */
    Collection<EventFullDto> searchEventsToAdmin(Integer[] users, String[] states, Integer[] categories,
                                                 String rangeStart, String rangeEnd, int from, int size);

    /**
     * Метод позволяет обновить событие по идентификатору
     *
     * @param eventId  идентификатор события
     * @param eventDto объект, описывающий свойства для обновления события, которые задает администратор
     * @return полная информация об обновленном объекте
     * @since 1.0
     */
    EventFullDto updateEventByAdmin(int eventId, AdminUpdateEventRequestDto eventDto);

    /**
     * Метод позволяет опубликовать событие, добавленное пользователей, по идентификатору и находящееся в состоянии
     * ожидания модерации
     *
     * @param eventId идентификатор события
     * @return полная информация об опубликованном событии
     * @since 1.0
     */
    EventFullDto publishEvent(int eventId);

    /**
     * Метод позволяет отклонить публикацию события, добавленного пользователем, по идентификатору
     *
     * @param eventId идентификатор события
     * @return полная информация об отклоненном событии
     * @since 1.0
     */
    EventFullDto rejectEvent(int eventId);

    /**
     * Метод позволяет получить краткую информацию о событиях пользователя, подходящих под заданные условия
     *
     * @param userId идентификатор пользователя {@link ru.practicum.ewm.models.User}
     * @param from   количество элементов, которые нужно пропустить для формирования текущего набора(по умолчанию 0)
     * @param size   количество элементов в наборе(по умолчанию 10)
     * @return краткая информация о событиях пользователя, подходящих под заданные условия
     * @since 1.0
     */
    Collection<EventShortDto> getUserEvents(int userId, int from, int size);

    /**
     * Метод позволяет пользователю обновить своё событие
     *
     * @param userId   идентификатор пользователя {@link ru.practicum.ewm.models.User}
     * @param eventDto объект Dto, описывающий свойства для обновления события {@link UpdateEventRequestDto}
     * @return полная информация по обновленному событию
     * @since 1.0
     */
    EventFullDto updateUserEvent(int userId, UpdateEventRequestDto eventDto);

    /**
     * Метод позволяет пользователю получить по идентификатору своего события полную информацию о нем
     *
     * @param userId  идентификатор пользователя {@link ru.practicum.ewm.models.User}
     * @param eventId идентификатор события {@link ru.practicum.ewm.models.Event}
     * @return полная информация о событии
     * @since 1.0
     */
    EventFullDto getUserEvent(int userId, int eventId);

    /**
     * Метод позволяет пользователю отменить свое неопубликованное событие
     *
     * @param userId  идентификатор пользователя {@link ru.practicum.ewm.models.User}
     * @param eventId идентификатор события {@link ru.practicum.ewm.models.Event}
     * @return полная информация об отмененном событии
     */
    EventFullDto cancelEventByUser(int userId, int eventId);
}
