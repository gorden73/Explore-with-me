package ru.practicum.ewm.repositories;

import org.springframework.stereotype.Repository;
import ru.practicum.ewm.models.Event;
import ru.practicum.ewm.models.EventState;

import java.util.List;

@Repository
public interface EventCustomRepository {
    List<Event> getAllEvents(String text, Integer[] categories, Boolean paid, String rangeStart,
                             String rangeEnd, boolean onlyAvailable, String sort, int from, int size);

    List<Event> searchEventsToAdmin(Integer[] users, EventState[] states, Integer[] categories, String rangeStart,
                                    String rangeEnd, int from, int size);
}
