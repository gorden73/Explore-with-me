package ru.practicum.ewm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.models.dto.categories.CategoryDto;
import ru.practicum.ewm.models.dto.complications.ComplicationDto;
import ru.practicum.ewm.models.dto.complications.NewComplicationDto;
import ru.practicum.ewm.models.dto.events.AdminUpdateEventRequestDto;
import ru.practicum.ewm.models.dto.events.EventFullDto;
import ru.practicum.ewm.models.dto.users.UserDto;
import ru.practicum.ewm.services.CategoryService;
import ru.practicum.ewm.services.ComplicationService;
import ru.practicum.ewm.services.EventService;
import ru.practicum.ewm.services.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;

@RestController
@RequestMapping(path = "/admin")
@Validated
public class AdminController {
    private final UserService userService;
    private final CategoryService categoryService;
    private final EventService eventService;
    private final ComplicationService complicationService;

    @Autowired
    public AdminController(UserService userService, CategoryService categoryService, EventService eventService,
                           ComplicationService complicationService) {
        this.userService = userService;
        this.categoryService = categoryService;
        this.eventService = eventService;
        this.complicationService = complicationService;
    }

    @GetMapping("/users")
    public Collection<UserDto> getAllUsers(@RequestParam(required = false)  Integer[] ids,
                                           @RequestParam(defaultValue = "0")
                                           @PositiveOrZero(message = "может быть равно или больше 0")
                                           int from,
                                           @RequestParam(defaultValue = "10")
                                           @Positive(message = "может быть только больше 0")
                                           int size) {
        return userService.getAllUsers(ids, from, size);
    }

    @PostMapping("/users")
    public UserDto addUser(@Valid @RequestBody UserDto userDto) {
        return userService.addUser(userDto);
    }

    @DeleteMapping("/users/{userId}")
    public void removeUser(@PathVariable int userId) {
        userService.removeUser(userId);
    }

    @PostMapping("/categories")
    public CategoryDto addCategory(@Valid @RequestBody CategoryDto categoryDto) {
        return categoryService.addCategory(categoryDto);
    }

    @PatchMapping("/categories")
    public CategoryDto updateCategory(@Valid @RequestBody CategoryDto categoryDto) {
        return categoryService.updateCategory(categoryDto);
    }

    @DeleteMapping("/categories/{catId}")
    public void removeCategory(@PathVariable int catId) {
        categoryService.removeCategory(catId);
    }

    @PostMapping("/complications")
    public ComplicationDto addComplication(@Valid @RequestBody NewComplicationDto complicationDto) {
        return complicationService.addComplication(complicationDto);
    }

    @DeleteMapping("/complications/{compId}")
    public void removeComplicationById(@PathVariable int compId) {
        complicationService.removeComplicationById(compId);
    }

    @DeleteMapping("/complications/{compId}/events/{eventId}")
    public void removeEventFromComplication(@PathVariable int compId,
                                            @PathVariable int eventId) {
        complicationService.removeEventFromComplication(compId, eventId);
    }

    @PatchMapping("/complications/{compId}/events/{eventId}")
    public void addEventToComplication(@PathVariable int compId,
                                       @PathVariable int eventId) {
        complicationService.addEventToComplication(compId, eventId);
    }

    @DeleteMapping("/complications/{compId}/pin")
    public void unpinComplicationAtMainPage(@PathVariable int compId) {
        complicationService.unpinComplicationAtMainPage(compId);
    }

    @PatchMapping("/complications/{compId}/pin")
    public void pinComplicationAtMainPage(@PathVariable int compId) {
        complicationService.pinComplicationAtMainPage(compId);
    }

    @GetMapping("/events")
    public Collection<EventFullDto> searchEvents(@RequestParam Integer[] users,
                                                 @RequestParam String[] states,
                                                 @RequestParam Integer[] categories,
                                                 @RequestParam String rangeStart,
                                                 @RequestParam String rangeEnd,
                                                 @RequestParam @PositiveOrZero int from,
                                                 @RequestParam @Positive int size) {
        return eventService.searchEventsToAdmin(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PutMapping("/events/{eventId}")
    public EventFullDto updateEvent(@PathVariable int eventId,
                                    @RequestBody AdminUpdateEventRequestDto eventDto) {
        return eventService.updateEventByAdmin(eventId, eventDto);
    }

    @PatchMapping("/events/{eventId}/publish")
    public EventFullDto publishEvent(@PathVariable int eventId) {
        return eventService.publishEvent(eventId);
    }

    @PatchMapping("/events/{eventId}/reject")
    public EventFullDto rejectEvent(@PathVariable int eventId) {
        return eventService.rejectEvent(eventId);
    }
}
