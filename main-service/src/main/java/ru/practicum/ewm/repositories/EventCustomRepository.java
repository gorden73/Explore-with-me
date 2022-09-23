package ru.practicum.ewm.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.models.Event;
import ru.practicum.ewm.models.EventState;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventCustomRepository {
    List<Event> getAllEvents(String text, Integer[] categories, boolean paid, String rangeStart,
                             String rangeEnd, boolean onlyAvailable, int from, int size);

    List<Event> searchEventsToAdmin(Integer[] users, EventState[] states, Integer[] categories, String rangeStart,
                                    String rangeEnd, int from, int size);
}
