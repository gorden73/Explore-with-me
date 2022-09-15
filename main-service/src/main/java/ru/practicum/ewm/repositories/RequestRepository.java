package ru.practicum.ewm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.models.Event;
import ru.practicum.ewm.models.Request;
import ru.practicum.ewm.models.RequestState;
import ru.practicum.ewm.models.User;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Integer> {
    List<Request> getRequestsByEventAndRequester(Event event, User requester);

    @Query(value = "select count(id) " +
            "from Request " +
            "where event = ?1 " +
            "and state like ('CONFIRM')")
    Integer getConfirmedRequests(int eventId);

    List<Request> findRequestsByEventAndState(Event event, RequestState state);

    List<Request> findRequestsByRequester(User requester);

    Request findRequestByIdAndRequester(int id, User requester);
}
