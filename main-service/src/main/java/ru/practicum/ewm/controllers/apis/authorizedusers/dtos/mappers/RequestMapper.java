package ru.practicum.ewm.controllers.apis.authorizedusers.dtos.mappers;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.controllers.apis.authorizedusers.dtos.requests.ParticipationRequestDto;
import ru.practicum.ewm.models.Request;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Класс для преобразования сущности запроса на участие в событии в Dto и обратно
 *
 * @see Request
 * @since 1.0
 */
@Component
public class RequestMapper {

    /**
     * Метод позволяет преобразовать сущность запроса на участие в событии в Dto запроса на участие в событии
     *
     * @param request сущность запроса на участие в событии
     * @return Dto запроса на участие в событии
     * @since 1.0
     */
    public static ParticipationRequestDto toDto(Request request) {
        ParticipationRequestDto dto = new ParticipationRequestDto(request.getEvent().getId(),
                request.getRequester().getId());
        dto.setCreated(request.getCreated().format(EventMapper.FORMATTER));
        dto.setId(request.getId());
        dto.setStatus(request.getState().toString());
        return dto;
    }

    /**
     * Метод позволяет преобразовать коллекцию сущностей запроса на участие в событии в коллекицию Dto запросов на
     * участие в событиях
     *
     * @param requests коллекция сущностей запроса на участие в событии
     * @return коллекицию Dto запросов на участие в событиях
     * @since 1.0
     */
    public static Collection<ParticipationRequestDto> toDtoCollection(Collection<Request> requests) {
        return requests.stream()
                .map(RequestMapper::toDto)
                .collect(Collectors.toList());
    }
}
