package ru.practicum.ewm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.models.Event;
import ru.practicum.ewm.models.Request;
import ru.practicum.ewm.models.RequestState;
import ru.practicum.ewm.models.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequestRepository extends JpaRepository<Request, Integer> {

    @Query(value = "select r " +
            "from Request r " +
            "left join Event e on r.event = e " +
            "where e = ?1 " +
            "and e.initiator = ?2")
    List<Request> getRequestsByEventAndEventInitiator(Event event, User initiator);

    Optional<Request> getRequestByEventAndRequester(Event event, User requester);

    @Query(value = "select count(id) " +
            "from requests " +
            "where event = ?1 " +
            "and state = 'CONFIRM'", nativeQuery = true)
    Integer getConfirmedRequests(int event);

    List<Request> findRequestsByEventAndState(Event event, RequestState state);

    List<Request> findRequestsByRequester(User requester);

    Optional<Request> findRequestByIdAndRequester(int id, User requester);
}
