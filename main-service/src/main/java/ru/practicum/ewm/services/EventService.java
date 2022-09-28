package ru.practicum.ewm.services;

import ru.practicum.ewm.apis.admins.dtos.events.AdminUpdateEventRequestDto;
import ru.practicum.ewm.apis.authorizedusers.dtos.events.NewEventDto;
import ru.practicum.ewm.apis.authorizedusers.dtos.events.UpdateEventRequestDto;
import ru.practicum.ewm.models.Event;
import ru.practicum.ewm.models.dtos.events.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

public interface EventService {
    Collection<EventShortDto> getAllEvents(String text, Integer[] categories, boolean paid, String rangeStart,
                                           String rangeEnd,
                                           boolean onlyAvailable, String sort, int from, int size,
                                           HttpServletRequest request);

    EventFullDto getFullEventById(int id, HttpServletRequest request);

    Event getEventById(int id);

    EventFullDto addEvent(int userId, NewEventDto eventDto);

    Collection<EventFullDto> searchEventsToAdmin(Integer[] users, String[] states, Integer[] categories,
                                                 String rangeStart, String rangeEnd, int from, int size);

    EventFullDto updateEventByAdmin(int eventId, AdminUpdateEventRequestDto eventDto);

    EventFullDto publishEvent(int eventId);

    EventFullDto rejectEvent(int eventId);

    Collection<EventShortDto> getUserEvents(int userId, int from, int size);

    EventFullDto updateUserEvent(int userId, UpdateEventRequestDto eventDto);

    EventFullDto getUserEvent(int userId, int eventId);

    EventFullDto cancelEventByUser(int userId, int eventId);
}
