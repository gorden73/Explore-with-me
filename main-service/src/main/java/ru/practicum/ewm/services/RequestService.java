package ru.practicum.ewm.services;

import ru.practicum.ewm.apis.authorizedusers.dtos.requests.ParticipationRequestDto;

import java.util.Collection;

/**
 * Интерфейс сервиса для работы с запросами на участие в событиях
 *
 * @see ParticipationRequestDto
 * @since 1.0
 */
public interface RequestService {
    /**
     * Метод позволяет авторизованному пользователю получить коллекцию Dto запросов на участие в его событии
     *
     * @param userId  идентификатор организатора события
     * @param eventId идентификатор события
     * @return коллекцию Dto запросов на участие в событии
     * @since 1.0
     */
    Collection<ParticipationRequestDto> getEventRequests(int userId, int eventId);

    /**
     * Метод позволяет авторизованному пользователю подтвердить запрос на участие в его событии
     *
     * @param userId  идентификатор организатора события
     * @param eventId идентификатор события
     * @param reqId   идентификатор запроса
     * @return Dto запроса на участие в событии
     * @since 1.0
     */
    ParticipationRequestDto confirmEventRequest(int userId, int eventId, int reqId);

    /**
     * Метод позволяет авторизованному пользователю отклонить запрос на участие в его событии
     *
     * @param userId  идентификатор организатора события
     * @param eventId идентификатор события
     * @param reqId   идентификатор запроса
     * @return Dto запроса на участие в событии
     * @since 1.0
     */
    ParticipationRequestDto rejectEventRequest(int userId, int eventId, int reqId);

    /**
     * Метод позволяет авторизованному пользователю получить список Dto всех своих запросов на участие в чужих событиях
     *
     * @param userId идентификатор пользователя
     * @return список Dto всех своих запросов на участие в чужих событиях
     * @since 1.0
     */
    Collection<ParticipationRequestDto> getUserRequests(int userId);

    /**
     * Метод позволяет авторизованному пользователю добавить запрос на участие в событии по идентификатору
     *
     * @param userId  идентификатор пользователя, создавшего запрос на участие в событии
     * @param eventId идентификатор события
     * @return Dto созданного запроса на участие в событии
     * @since 1.0
     */
    ParticipationRequestDto addRequest(int userId, int eventId);

    /**
     * Метод позволяет авторизованному пользователю отменить свой запрос на участие в событии по идентификатору
     *
     * @param userId    идентификатор пользователя, создавшего запрос на участие в событии
     * @param requestId идентификатор запроса на участие в событии
     * @return Dto запроса на участие в событии
     * @since 1.0
     */
    ParticipationRequestDto cancelRequestByUser(int userId, int requestId);
}
