package ru.practicum.ewm.apis.authorizedusers.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.apis.authorizedusers.dtos.requests.ParticipationRequestDto;
import ru.practicum.ewm.services.RequestService;

import java.util.Collection;

@RestController
@RequestMapping(path = "/users/{userId}/")
public class AuthorizedUserRequestController {
    private final RequestService requestService;

    @Autowired
    public AuthorizedUserRequestController(RequestService requestService) {
        this.requestService = requestService;
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
}
