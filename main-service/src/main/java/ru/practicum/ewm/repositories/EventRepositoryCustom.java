package ru.practicum.ewm.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.models.Event;
import ru.practicum.ewm.models.EventState;

import java.util.List;

@Repository
public interface EventRepositoryCustom {
    List<Event> getAllEvents(String text, Integer[] categories, boolean paid, String rangeStart,
                                            String rangeEnd, boolean onlyAvailable, /*EventState state,*/
                                            Pageable page);
}
