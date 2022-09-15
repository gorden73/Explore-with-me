package ru.practicum.ewm.services;

import ru.practicum.ewm.models.dto.events.*;

import java.util.Collection;

public interface EventService {
    Collection<EventShortDto> getAllEvents(String text, Integer[] categories, boolean paid, String rangeStart,
                                           String rangeEnd,
                                           boolean onlyAvailable, String sort, int from, int size);

    EventFullDto getEventById(int id);

    EventFullDto addEvent(int userId, NewEventDto eventDto);

    Collection<EventFullDto> searchEventsToAdmin(Integer[] users, String[] states,Integer[] categories,
                                                 String rangeStart, String rangeEnd, int from, int size);

    EventFullDto updateEventByAdmin(int eventId, AdminUpdateEventRequestDto eventDto);

    EventFullDto publishEvent(int eventId);

    EventFullDto rejectEvent(int eventId);

    Collection<Integer> getUserEvents(int userId, int from, int size);

    EventFullDto updateUserEvent(int userId, UpdateEventRequestDto eventDto) throws IllegalAccessException;

    EventFullDto getUserEvent(int userId, int eventId);

    EventFullDto cancelEventByUser(int userId, int eventId);
}
