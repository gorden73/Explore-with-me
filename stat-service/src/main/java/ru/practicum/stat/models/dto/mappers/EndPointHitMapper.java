package ru.practicum.stat.models.dto.mappers;

import org.springframework.stereotype.Component;
import ru.practicum.stat.models.EndPointHit;
import ru.practicum.stat.models.dto.EndPointHitDto;

import java.time.Instant;

@Component
public class EndPointHitMapper {

    public static EndPointHitDto toDto(EndPointHit endPointHit) {
        EndPointHitDto dto = new EndPointHitDto(
                endPointHit.getApp(),
                endPointHit.getUri(),
                endPointHit.getIp());
        dto.setId(endPointHit.getId());
        dto.setTimestamp(endPointHit.getTimestamp());
        return dto;
    }

    public static EndPointHit toEndPointHit(EndPointHitDto dto) {
        return new EndPointHit(
                dto.getApp(),
                dto.getUri(),
                dto.getIp(),
                Instant.now().toEpochMilli());
    }
}
