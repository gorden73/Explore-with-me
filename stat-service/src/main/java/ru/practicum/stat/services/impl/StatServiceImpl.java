package ru.practicum.stat.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.stat.models.EndPointHit;
import ru.practicum.stat.models.dto.EndPointHitDto;
import ru.practicum.stat.models.ViewStats;
import ru.practicum.stat.models.dto.ViewStatsDto;
import ru.practicum.stat.models.dto.mappers.EndPointHitMapper;
import ru.practicum.stat.models.dto.mappers.ViewStatsMapper;
import ru.practicum.stat.repositories.StatRepository;
import ru.practicum.stat.services.StatService;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class StatServiceImpl implements StatService {
    private final StatRepository statRepository;

    @Autowired
    public StatServiceImpl(StatRepository statRepository) {
        this.statRepository = statRepository;
    }

    @Override
    public EndPointHitDto addEndPointHit(EndPointHitDto dto) {
        EndPointHit endPointHit = statRepository.save(EndPointHitMapper.toEndPointHit(dto));
        log.info("Добавлен endpoint app={}, uri={}, ip={}.", endPointHit.getApp(), endPointHit.getUri(),
                endPointHit.getIp());
        return EndPointHitMapper.toDto(endPointHit);
    }

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

    private String decodeParameter(String param) {
        if (param != null) {
            if (!param.isBlank() && !param.isEmpty()) {
                try {
                    return URLDecoder.decode(param, StandardCharsets.UTF_8.toString());
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    throw new RuntimeException("Неверный формат кодировки.");
                }
            }
        }
        throw new RuntimeException("Неверно задан параметр.");
    }
}
