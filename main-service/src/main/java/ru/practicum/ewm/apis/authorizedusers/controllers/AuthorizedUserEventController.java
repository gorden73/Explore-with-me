package ru.practicum.ewm.apis.authorizedusers.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.models.dtos.events.EventFullDto;
import ru.practicum.ewm.models.dtos.events.EventShortDto;
import ru.practicum.ewm.apis.authorizedusers.dtos.events.NewEventDto;
import ru.practicum.ewm.apis.authorizedusers.dtos.events.UpdateEventRequestDto;
import ru.practicum.ewm.services.EventService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;

@RestController
@RequestMapping(path = "/users/{userId}/events")
@Validated
public class AuthorizedUserEventController {
    private final EventService eventService;

    @Autowired
    public AuthorizedUserEventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public Collection<EventShortDto> getUserEvents(@PathVariable int userId,
                                                   @RequestParam(defaultValue = "0")
                                                   @PositiveOrZero(message = "может быть равно или больше 0") int from,
                                                   @RequestParam(defaultValue = "10")
                                                   @Positive(message = "может быть только больше 0") int size) {
        return eventService.getUserEvents(userId, from, size);
    }

    @PatchMapping
    public EventFullDto updateUserEvent(@PathVariable int userId,
                                        @Valid @RequestBody UpdateEventRequestDto eventDto) {
        return eventService.updateUserEvent(userId, eventDto);
    }

    @PostMapping
    public EventFullDto addEvent(@PathVariable int userId,
                                 @Valid @RequestBody NewEventDto eventDto) {
        return eventService.addEvent(userId, eventDto);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getUserEvent(@PathVariable int userId,
                                     @PathVariable int eventId) {
        return eventService.getUserEvent(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto cancelEventByUser(@PathVariable int userId,
                                          @PathVariable int eventId) {
        return eventService.cancelEventByUser(userId, eventId);
    }
}
