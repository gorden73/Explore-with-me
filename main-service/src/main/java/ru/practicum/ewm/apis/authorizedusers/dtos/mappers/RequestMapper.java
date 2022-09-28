package ru.practicum.ewm.apis.authorizedusers.dtos.mappers;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.models.Request;
import ru.practicum.ewm.apis.authorizedusers.dtos.requests.ParticipationRequestDto;

import java.util.Collection;
import java.util.stream.Collectors;

@Component
public class RequestMapper {

    public static ParticipationRequestDto toDto(Request request) {
        ParticipationRequestDto dto = new ParticipationRequestDto(request.getEvent().getId(),
                request.getRequester().getId());
        dto.setCreated(request.getCreated().toString());
        dto.setId(request.getId());
        dto.setStatus(request.getState().toString());
        return dto;
    }

    public static Collection<ParticipationRequestDto> toDtoCollection(Collection<Request> requests) {
        return requests.stream()
                .map(RequestMapper::toDto)
                .collect(Collectors.toList());
    }
}
