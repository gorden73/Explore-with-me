package ru.practicum.ewm.apis.admins.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.apis.admins.dtos.events.AdminUpdateEventRequestDto;
import ru.practicum.ewm.models.dtos.events.EventFullDto;
import ru.practicum.ewm.services.EventService;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;

@RestController
@RequestMapping(path = "/admin/events")
public class AdminEventController {
    private final EventService eventService;

    @Autowired
    public AdminEventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public Collection<EventFullDto> searchEvents(@RequestParam @NotEmpty(message = "не должно быть пустым")
                                                 Integer[] users,
                                                 @RequestParam @NotEmpty(message = "не должно быть пустым")
                                                 String[] states,
                                                 @RequestParam @NotEmpty(message = "не должно быть пустым")
                                                 Integer[] categories,
                                                 @RequestParam
                                                 @DateTimeFormat(pattern = "yyyy.MM.dd HH:mm:ss")
                                                 String rangeStart,
                                                 @RequestParam
                                                 @DateTimeFormat(pattern = "yyyy.MM.dd HH:mm:ss")
                                                 String rangeEnd,
                                                 @RequestParam
                                                 @PositiveOrZero(message = "может быть равно или больше 0")
                                                 int from,
                                                 @RequestParam
                                                 @Positive(message = "может быть только больше 0")
                                                 int size) {
        return eventService.searchEventsToAdmin(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PutMapping("/{eventId}")
    public EventFullDto updateEvent(@PathVariable int eventId,
                                    @RequestBody AdminUpdateEventRequestDto eventDto) {
        return eventService.updateEventByAdmin(eventId, eventDto);
    }

    @PatchMapping("/{eventId}/publish")
    public EventFullDto publishEvent(@PathVariable int eventId) {
        return eventService.publishEvent(eventId);
    }

    @PatchMapping("/{eventId}/reject")
    public EventFullDto rejectEvent(@PathVariable int eventId) {
        return eventService.rejectEvent(eventId);
    }
}
