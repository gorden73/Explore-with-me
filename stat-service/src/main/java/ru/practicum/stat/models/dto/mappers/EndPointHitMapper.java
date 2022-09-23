package ru.practicum.stat.models.dto.mappers;

import org.springframework.stereotype.Component;
import ru.practicum.stat.models.EndPointHit;
import ru.practicum.stat.models.dto.EndPointHitDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class EndPointHitMapper {

    public static EndPointHitDto toDto(EndPointHit endPointHit) {
        EndPointHitDto dto = new EndPointHitDto(
                endPointHit.getApp(),
                endPointHit.getUri(),
                endPointHit.getIp(),
                endPointHit.getTimestamp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        dto.setId(endPointHit.getId());
        return dto;
    }

    public static EndPointHit toEndPointHit(EndPointHitDto dto) {
        return new EndPointHit(
                dto.getApp(),
                dto.getUri(),
                dto.getIp(),
                LocalDateTime.parse(dto.getTimestamp(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }
}
