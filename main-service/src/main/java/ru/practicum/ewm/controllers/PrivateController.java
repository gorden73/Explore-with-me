package ru.practicum.ewm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.models.dto.events.EventFullDto;
import ru.practicum.ewm.models.dto.events.EventShortDto;
import ru.practicum.ewm.models.dto.events.NewEventDto;
import ru.practicum.ewm.models.dto.events.UpdateEventRequestDto;
import ru.practicum.ewm.models.dto.likes.LikeDto;
import ru.practicum.ewm.models.dto.requests.ParticipationRequestDto;
import ru.practicum.ewm.services.EventService;
import ru.practicum.ewm.services.LikeService;
import ru.practicum.ewm.services.RequestService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;
import java.util.List;

@RestController
@Validated
@RequestMapping(path = "/users/{userId}")
public class PrivateController {
    private final EventService eventService;
    private final RequestService requestService;
    private final LikeService likeService;

    @Autowired
    public PrivateController(EventService eventService, RequestService requestService, LikeService likeService) {
        this.eventService = eventService;
        this.requestService = requestService;
        this.likeService = likeService;
    }

    @GetMapping("/events")
    public Collection<EventShortDto> getUserEvents(@PathVariable int userId,
                                                   @RequestParam(defaultValue = "0")
                                                   @PositiveOrZero(message = "может быть равно или больше 0") int from,
                                                   @RequestParam(defaultValue = "10")
                                                   @Positive(message = "может быть только больше 0") int size) {
        return eventService.getUserEvents(userId, from, size);
    }

    @PatchMapping("/events")
    public EventFullDto updateUserEvent(@PathVariable int userId,
                                        @Valid @RequestBody UpdateEventRequestDto eventDto) {
        return eventService.updateUserEvent(userId, eventDto);
    }

    @PostMapping("/events")
    public EventFullDto addEvent(@PathVariable int userId,
                                 @Valid @RequestBody NewEventDto eventDto) {
        return eventService.addEvent(userId, eventDto);
    }

    @GetMapping("/events/{eventId}")
    public EventFullDto getUserEvent(@PathVariable int userId,
                                     @PathVariable int eventId) {
        return eventService.getUserEvent(userId, eventId);
    }

    @PatchMapping("/events/{eventId}")
    public EventFullDto cancelEventByUser(@PathVariable int userId,
                                          @PathVariable int eventId) {
        return eventService.cancelEventByUser(userId, eventId);
    }

    @GetMapping("/events/{eventId}/requests")
    public Collection<ParticipationRequestDto> getEventRequests(@PathVariable int userId,
                                                                @PathVariable int eventId) {
        return requestService.getEventRequests(userId, eventId);
    }

    @PatchMapping("/events/{eventId}/requests/{reqId}/confirm")
    public ParticipationRequestDto confirmEventRequest(@PathVariable int userId,
                                                       @PathVariable int eventId,
                                                       @PathVariable int reqId) {
        return requestService.confirmEventRequest(userId, eventId, reqId);
    }

    @PatchMapping("/events/{eventId}/requests/{reqId}/reject")
    public ParticipationRequestDto rejectEventRequest(@PathVariable int userId,
                                                      @PathVariable int eventId,
                                                      @PathVariable int reqId) {
        return requestService.rejectEventRequest(userId, eventId, reqId);
    }

    @GetMapping("/requests")
    public Collection<ParticipationRequestDto> getUserRequests(@PathVariable int userId) {
        return requestService.getUserRequests(userId);
    }

    @PostMapping("/requests")
    public ParticipationRequestDto addRequest(@PathVariable int userId,
                                              @RequestParam int eventId) {
        return requestService.addRequest(userId, eventId);
    }

    @PatchMapping("/requests/{requestId}/cancel")
    public ParticipationRequestDto cancelRequestByUser(@PathVariable int userId,
                                                       @PathVariable int requestId) {
        return requestService.cancelRequestByUser(userId, requestId);
    }

    /**
     * Метод позволяет пользователю поставить лайк чужому событию
     *
     * @param userId  идентификатор пользователя
     * @param eventId идентификатор события
     * @return краткая информация о событии, которому пользователь поставил лайк
     */
    @PostMapping("/events/{eventId}/like")
    public EventShortDto addLikeToEvent(@PathVariable int userId,
                                        @PathVariable int eventId) {
        return likeService.addLike(userId, eventId);
    }

    /**
     * Метод позволяет пользователю поставить дизлайк чужому событию
     *
     * @param userId  идентификатор пользователя
     * @param eventId идентификатор события
     * @return краткая информация о событии, которому пользователь поставил дизлайк
     */
    @PostMapping("/events/{eventId}/dislike")
    public EventShortDto addDislikeToEvent(@PathVariable int userId,
                                           @PathVariable int eventId) {
        return likeService.addDislike(userId, eventId);
    }

    /**
     * Метод позволяет пользователю получить краткую информацию по всем лайкам своего события
     *
     * @param userId  идентификатор пользователя
     * @param eventId идентификатор события
     * @return краткая информация по всем лайкам указанного события
     */
    @GetMapping("/events/{eventId}/like")
    public List<LikeDto> getEventLikes(@PathVariable @NotNull(message = "должен быть указан id пользователя")
                                       Integer userId,
                                       @PathVariable int eventId) {
        return likeService.getEventLikesDto(userId, eventId);
    }

    /**
     * Метод позволяет пользователю получить краткую информацию по всем дизлайкам своего события
     *
     * @param userId  идентификатор пользователя
     * @param eventId идентификатор события
     * @return краткая информация по всем дизлайкам указанного события
     */
    @GetMapping("/events/{eventId}/dislike")
    public List<LikeDto> getEventDislikes(@PathVariable @NotNull(message = "должен быть указан id пользователя")
                                          Integer userId,
                                          @PathVariable int eventId) {
        return likeService.getEventDislikesDto(userId, eventId);
    }
}
