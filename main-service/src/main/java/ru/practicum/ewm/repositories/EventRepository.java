package ru.practicum.ewm.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.models.Category;
import ru.practicum.ewm.models.Event;
import ru.practicum.ewm.models.EventState;
import ru.practicum.ewm.models.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer>, EventRepositoryCustom {
    /*@Query(value = "select e " +
            "from Event e " +
            "where (upper(e.annotation) like upper(concat('%', ?1, '%')) " +
            "or upper(e.description) like upper(concat('%', ?1, '%'))) " +
            "and e.category in ?2 " +
            "and e.paid = ?3 " +
            "and e.eventDate > ?4 " +
            "and e.eventDate < ?5 " +
            "and e.isAvailable = ?6 " +
            "and e.state = ?7 " +
            "group by e.eventDate " +
            "order by e.eventDate desc")
    List<Event> getAllEventsSortByEventDate(String text, Integer[] categories, boolean paid, LocalDateTime rangeStart,
                                            LocalDateTime rangeEnd, boolean onlyAvailable, EventState state,
                                            Pageable page);*/


    /*@Query(value = "select e " +
            "from Event e " +
            "where (upper(e.annotation) like upper(concat('%', ?1, '%')) " +
            "or upper(e.description) like upper(concat('%', ?1, '%'))) " +
            "and e.category in ?2 " +
            "and e.paid = ?3 " +
            "and e.eventDate > ?4 " +
            "and e.eventDate < ?5 " +
            "and e.isAvailable = ?6 " +
            "and e.state = ?7 ")
    List<Event> getAllEventsUnsorted(String text, Integer[] categories, boolean paid, LocalDateTime rangeStart,
                                     LocalDateTime rangeEnd, boolean onlyAvailable, EventState state,
                                     Pageable page);*/

    /*@Query(value = "select e " +
            "from Event e " +
            "where (upper(e.annotation) like upper(concat('%', ?1, '%')) " +
            "or upper(e.description) like upper(concat('%', ?1, '%'))) " +
            "and e.category in ?2 " +
            "and e.paid = ?3 " +
            "and e.eventDate > ?4 " +
            "and e.isAvailable = ?5 " +
            "and e.state = ?6 " +
            "group by e.eventDate " +
            "order by e.eventDate desc")
    List<Event> getAllEventsSortByEventDate(String text, Integer[] categories, boolean paid, LocalDateTime time,
                                            boolean onlyAvailable, EventState state, Pageable page);

    @Query(value = "select e " +
            "from Event e " +
            "where (upper(e.annotation) like upper(concat('%', ?1, '%')) " +
            "or upper(e.description) like upper(concat('%', ?1, '%'))) " +
            "and e.category in ?2 " +
            "and e.paid = ?3 " +
            "and e.eventDate > ?4 " +
            "and e.isAvailable = ?5 " +
            "and e.state = ?6 ")
    List<Event> getAllEventsUnsorted(String text, Integer[] categories, boolean paid, LocalDateTime time,
                                     boolean onlyAvailable, EventState state, Pageable page);*/

    @Query(value = "select e " +
            "from Event e " +
            "where e.initiator in ?1 " +
            "and e.state in ?2 " +
            "and e.category in ?3 " +
            "and e.eventDate > ?4 " +
            "and e.eventDate < ?5")
    List<Event> searchEventsToAdmin(Integer[] users, String[] states, Integer[] categories,
                                    String rangeStart, String rangeEnd, Pageable page);


    @Query(value = "select e " +
            "from Event e " +
            "where e.initiator = ?1")
    List<Event> findEventsByInitiator(User initiator, Pageable page);

    Optional<Event> findEventByIdAndInitiator(int id, User initiator);

    @Query(value = "select id " +
            "from Event " +
            "where category = ?1")
    List<Integer> findEventsByCategory(Category category);
}
