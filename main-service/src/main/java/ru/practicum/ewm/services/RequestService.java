package ru.practicum.ewm.services;

import ru.practicum.ewm.models.dto.requests.ParticipationRequestDto;

import java.util.Collection;

public interface RequestService {
    Collection<ParticipationRequestDto> getEventRequests(int userId, int eventId);

    ParticipationRequestDto confirmEventRequest(int userId, int eventId, int reqId);

    ParticipationRequestDto rejectEventRequest(int userId, int eventId, int reqId);

    Collection<ParticipationRequestDto> getUserRequests(int userId);

    ParticipationRequestDto addRequest(int userId, ParticipationRequestDto requestDto);

    ParticipationRequestDto cancelRequestByUser(int userId, int requestId);
}
