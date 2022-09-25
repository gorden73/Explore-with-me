package ru.practicum.ewm.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.models.Category;
import ru.practicum.ewm.models.Event;
import ru.practicum.ewm.models.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer>, EventCustomRepository {

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
