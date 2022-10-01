package ru.practicum.stat.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.stat.models.EndPointHit;
import ru.practicum.stat.models.ViewStats;
import ru.practicum.stat.models.dto.EndPointHitDto;
import ru.practicum.stat.models.dto.ViewStatsDto;
import ru.practicum.stat.models.dto.mappers.EndPointHitMapper;
import ru.practicum.stat.models.dto.mappers.ViewStatsMapper;
import ru.practicum.stat.repositories.StatRepository;
import ru.practicum.stat.services.StatService;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Класс сервиса для работы со статистикой просмотров эндпоинтов, реазилующий интерфейс {@link StatService}
 *
 * @since 1.0
 */
@Service
@Slf4j
public class StatServiceImpl implements StatService {

    /**
     * Интерфейс репозитория статистики, наследующий от {@link org.springframework.data.jpa.repository.JpaRepository}
     *
     * @since 1.0
     */
    private final StatRepository statRepository;

    @Autowired
    public StatServiceImpl(StatRepository statRepository) {
        this.statRepository = statRepository;
    }

    /**
     * Метод позволяет добавить информацию о просмотре определенного эндпоинта
     *
     * @param dto Dto записи данных о просмотрах эндпоинтов
     * @return Dto записи данных о просмотрах эндпоинтов
     * @since 1.0
     */
    @Override
    public EndPointHitDto addEndPointHit(EndPointHitDto dto) {
        EndPointHit endPointHit = statRepository.save(EndPointHitMapper.toEndPointHit(dto));
        log.info("Добавлен endpoint app={}, uri={}, ip={}.", endPointHit.getApp(), endPointHit.getUri(),
                endPointHit.getIp());
        return EndPointHitMapper.toDto(endPointHit);
    }

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
    @Override
    public Collection<ViewStatsDto> getStats(String start, String end, String[] uris, Boolean unique) {
        List<ViewStats> viewStats = new ArrayList<>();
        for (String uri : uris) {
            List<EndPointHit> endPointHits = statRepository.findAllByUri(decodeParameter(start), decodeParameter(end),
                    uri, unique);
            if (!endPointHits.isEmpty()) {
                viewStats.add(new ViewStats(endPointHits.get(0).getApp(), uri, endPointHits.size()));
            }
        }
        return viewStats.stream()
                .map(ViewStatsMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Метод позволяет декодировать переданный параметр, закодированный URLDecoder
     *
     * @param param переданный параметр
     * @return декодированный параметр
     * @since 1.0
     */
    private String decodeParameter(String param) {
        if (param != null) {
            if (!param.isBlank() && !param.isEmpty()) {
                try {
                    return URLDecoder.decode(param, StandardCharsets.UTF_8.toString());
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    throw new RuntimeException("Неверный формат кодировки времени.");
                }
            }
        }
        throw new RuntimeException("Неверный формат кодировки времени.");
    }
}
