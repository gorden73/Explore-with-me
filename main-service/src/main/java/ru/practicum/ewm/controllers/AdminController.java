package ru.practicum.ewm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.models.dto.categories.CategoryDto;
import ru.practicum.ewm.models.dto.compilations.CompilationDto;
import ru.practicum.ewm.models.dto.compilations.NewCompilationDto;
import ru.practicum.ewm.models.dto.events.AdminUpdateEventRequestDto;
import ru.practicum.ewm.models.dto.events.EventFullDto;
import ru.practicum.ewm.models.dto.likes.AdminLikeDto;
import ru.practicum.ewm.models.dto.users.UserDto;
import ru.practicum.ewm.services.CategoryService;
import ru.practicum.ewm.services.CompilationService;
import ru.practicum.ewm.services.EventService;
import ru.practicum.ewm.services.UserService;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping(path = "/admin")
@Validated
public class AdminController {
    private final UserService userService;
    private final CategoryService categoryService;
    private final EventService eventService;
    private final CompilationService compilationService;

    @Autowired
    public AdminController(UserService userService, CategoryService categoryService, EventService eventService,
                           CompilationService compilationService) {
        this.userService = userService;
        this.categoryService = categoryService;
        this.eventService = eventService;
        this.compilationService = compilationService;
    }

    @GetMapping("/users")
    public Collection<UserDto> getAllUsers(@RequestParam(required = false) Integer[] ids,
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

    @PostMapping("/compilations")
    public CompilationDto addCompilation(@Valid @RequestBody NewCompilationDto compilationDto) {
        return compilationService.addCompilation(compilationDto);
    }

    @DeleteMapping("/compilations/{compId}")
    public void removeCompilationById(@PathVariable int compId) {
        compilationService.removeCompilationById(compId);
    }

    @DeleteMapping("/compilations/{compId}/events/{eventId}")
    public void removeEventFromCompilation(@PathVariable int compId,
                                           @PathVariable int eventId) {
        compilationService.removeEventFromCompilation(compId, eventId);
    }

    @PatchMapping("/compilations/{compId}/events/{eventId}")
    public void addEventToCompilation(@PathVariable int compId,
                                      @PathVariable int eventId) {
        compilationService.addEventToCompilation(compId, eventId);
    }

    @DeleteMapping("/compilations/{compId}/pin")
    public void unpinCompilationAtMainPage(@PathVariable int compId) {
        compilationService.unpinCompilationAtMainPage(compId);
    }

    @PatchMapping("/compilations/{compId}/pin")
    public void pinCompilationAtMainPage(@PathVariable int compId) {
        compilationService.pinCompilationAtMainPage(compId);
    }

    @GetMapping("/events")
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

    /**
     * Метод позволяет получить подробную информацию по всем лайкам указанного события
     *
     * @param eventId идентификатор события
     * @return подробная информация по всем лайкам указанного события
     */
    @GetMapping("/events/{eventId}/like")
    public List<AdminLikeDto> getEventLikes(@PathVariable int eventId) {
        return eventService.getEventAdminLikesDto(null, eventId);
    }

    /**
     * Метод позволяет получить подробную информацию по всем дизлайкам указанного события
     *
     * @param eventId идентификатор события
     * @return подробная информация по всем дизлайкам указанного события
     */
    @GetMapping("/events/{eventId}/dislike")
    public List<AdminLikeDto> getEventDislikes(@PathVariable int eventId) {
        return eventService.getEventAdminDislikesDto(null, eventId);
    }
}
