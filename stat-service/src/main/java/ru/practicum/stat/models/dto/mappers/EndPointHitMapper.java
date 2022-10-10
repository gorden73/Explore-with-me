package ru.practicum.stat.models.dto.mappers;

import org.springframework.stereotype.Component;
import ru.practicum.stat.models.EndPointHit;
import ru.practicum.stat.models.dto.EndPointHitDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Класс для работы с записями данных о просмотрах эндпоинтов и преобразования их в Dto и обратно
 *
 * @see EndPointHit
 * @see EndPointHitDto
 * @since 1.0
 */
@Component
public class EndPointHitMapper {

    /**
     * Метод позволяет преобразовать сущность записи данных о просмотрах в Dto
     *
     * @param endPointHit сущность записи данных о просмотрах
     * @return Dto сущности записи данных о просмотрах
     * @since 1.0
     */
    public static EndPointHitDto toDto(EndPointHit endPointHit) {
        EndPointHitDto dto = new EndPointHitDto(
                endPointHit.getApp(),
                endPointHit.getUri(),
                endPointHit.getIp(),
                endPointHit.getTimestamp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        dto.setId(endPointHit.getId());
        return dto;
    }

    /**
     * Метод позволяет преобразовать Dto записи данных о просмотрах в сущность
     *
     * @param dto Dto сущности записи данных о просмотрах
     * @return сущность записи данных о просмотрах
     * @since 1.0
     */
    public static EndPointHit toEndPointHit(EndPointHitDto dto) {
        return new EndPointHit(
                dto.getApp(),
                dto.getUri(),
                dto.getIp(),
                LocalDateTime.parse(dto.getTimestamp(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }
}
