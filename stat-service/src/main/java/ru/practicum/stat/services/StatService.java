package ru.practicum.stat.services;

import ru.practicum.stat.models.dto.EndPointHitDto;
import ru.practicum.stat.models.dto.ViewStatsDto;

import java.util.Collection;

/**
 * Интерфейс для работы со статистикой просмотров эндпоинтов
 *
 * @see EndPointHitDto
 * @see ViewStatsDto
 * @since 1.0
 */
public interface StatService {

    /**
     * Метод позволяет добавить информацию о просмотре определенного эндпоинта
     *
     * @param dto Dto записи данных о просмотрах эндпоинтов
     * @return Dto записи данных о просмотрах эндпоинтов
     * @since 1.0
     */
    EndPointHitDto addEndPointHit(EndPointHitDto dto);

    /**
     * Метод позволяет получить статистику просмотров определенных эндпоинтов, подходящую под указанные параметры
     *
     * @param start  дата и время, не раньше которых должна быть добавлена информация о просмотрах эндпоинтов
     * @param end    дата и время, не раньше которых должна быть добавлена информация о просмотрах эндпоинтов
     * @param uris   список URI, для которых надо предоставить статистику просмотров
     * @param unique выполнять поиск только для уникальных просмотров (по уникальным Ip-адресам)
     * @return статистика просмотров определенных эндпоинтов
     * @since 1.0
     */
    Collection<ViewStatsDto> getStats(String start, String end, String[] uris, Boolean unique);
}
