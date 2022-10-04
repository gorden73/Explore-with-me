package ru.practicum.ewm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.models.*;

import java.util.List;
import java.util.Optional;

/**
 * Интерфейс для работы с репозиторием запросов на участие в событии, наследующий {@link JpaRepository}
 *
 * @since 1.0
 */
@Repository
public interface RequestRepository extends JpaRepository<Request, Integer> {

    /**
     * Метод позволяет получить список запросов по событию и организатору события
     *
     * @param event     событие
     * @param initiator организатор события
     * @return список запросов
     * @since 1.0
     */
    @Query(value = "select r " +
            "from Request r " +
            "left join Event e on r.event = e " +
            "where e = ?1 " +
            "and e.initiator = ?2")
    List<Request> getRequestsByEventAndEventInitiator(Event event, User initiator);

    /**
     * Метод позволяет получить запрос на участие в переданном событии и пользователю, создавшему запрос
     * (если такой есть)
     *
     * @param event     событие
     * @param requester пользователь, создавший запрос
     * @return запрос на участие в событии или null (если такого запроса не найдено)
     * @since 1.0
     */
    Optional<Request> getRequestByEventAndRequester(Event event, User requester);

    /**
     * Метод позволяет получить количество подтвержденных запросов на участие в событии
     *
     * @param event идентификатор события
     * @return количество подтвержденных запросов
     * @since 1.0
     */
    @Query(value = "select count(id) " +
            "from requests " +
            "where event = ?1 " +
            "and state = 'CONFIRMED'", nativeQuery = true)
    Integer getConfirmedRequests(int event);

    /**
     * Метод позволяет получить запрос запрос пользователя на участие в событии по статусу запроса
     *
     * @param event событие
     * @param state статус запроса
     * @return запрос на участие в событии
     * @since 1.1
     */
    Optional<Request> getRequestByEventAndRequesterAndState(Event event, User requester, RequestState state);

    /**
     * Метод позволяет получить список запросов по событию и статусу запросов на участие в событии
     *
     * @param event событие
     * @param state статус запросов на участие в событии
     * @return список запросов на участие в событии
     * @since 1.0
     */
    List<Request> findRequestsByEventAndState(Event event, RequestState state);

    /**
     * Метод позволяет получить список запросов на участие в событиях определенного пользователя
     *
     * @param requester пользователь, создавший запросы на участие в событиях
     * @return список запросов на участие в событиях
     * @since 1.0
     */
    List<Request> findRequestsByRequester(User requester);

    /**
     * Метод позволяет получить запрос на участие в событии по идентификатору запроса и пользователю, создавшему запрос
     *
     * @param id        идентификатор запроса на участие в событии
     * @param requester пользователь, создавший запрос
     * @return запрос на участие в событии или null (если такого запроса не найдено)
     * @since 1.0
     */
    Optional<Request> findRequestByIdAndRequester(int id, User requester);
}
