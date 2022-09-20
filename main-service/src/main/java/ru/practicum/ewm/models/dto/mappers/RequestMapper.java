package ru.practicum.ewm.models.dto.mappers;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.models.Request;
import ru.practicum.ewm.models.RequestState;
import ru.practicum.ewm.models.dto.requests.ParticipationRequestDto;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;

@Component
public class RequestMapper {
   /* public static Request toRequest(ParticipationRequestDto dto) {
        return new Request(
                LocalDateTime.now(),
                RequestState.PENDING);
    }*/

    public static ParticipationRequestDto toDto(Request request) {
        ParticipationRequestDto dto = new ParticipationRequestDto(request.getEvent().getId(),
                request.getRequester().getId());
        dto.setCreated(request.getCreated().toString());
        dto.setId(request.getId());
        dto.setState(request.getState().toString());
        return dto;
    }

    public static Collection<ParticipationRequestDto> toDtoCollection(Collection<Request> requests) {
        return requests.stream()
                .map(RequestMapper::toDto)
                .collect(Collectors.toList());
    }
}
