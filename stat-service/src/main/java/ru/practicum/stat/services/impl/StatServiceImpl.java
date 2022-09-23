package ru.practicum.stat.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.stat.models.EndPointHit;
import ru.practicum.stat.models.dto.EndPointHitDto;
import ru.practicum.stat.models.ViewStats;
import ru.practicum.stat.models.dto.mappers.EndPointHitMapper;
import ru.practicum.stat.repositories.StatRepository;
import ru.practicum.stat.repositories.ViewStatsRepository;
import ru.practicum.stat.services.StatService;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class StatServiceImpl implements StatService {
    private final StatRepository statRepository;
    private final ViewStatsRepository viewStatsRepository;

    @Autowired
    public StatServiceImpl(StatRepository statRepository, ViewStatsRepository viewStatsRepository) {
        this.statRepository = statRepository;
        this.viewStatsRepository = viewStatsRepository;
    }

    @Override
    public EndPointHitDto addEndPointHit(EndPointHitDto dto) {
        List<EndPointHit> returnedEndpoints = statRepository.findAllByUri(dto.getUri());
        EndPointHit endPointHit = statRepository.save(EndPointHitMapper.toEndPointHit(dto));
        log.info("Добавлен endpoint app={}, uri={}, ip={}.", endPointHit.getApp(), endPointHit.getUri(),
                endPointHit.getIp());
        if (returnedEndpoints.isEmpty()) {
            ViewStats viewStats = new ViewStats(dto.getApp(), dto.getUri());
            viewStats.setHits(1);
            viewStats.setUniqueHits(1);
            viewStatsRepository.save(viewStats);
        } else {
            ViewStats viewStats1 = viewStatsRepository.findViewStatsByUri(dto.getUri());
            viewStats1.setHits(returnedEndpoints.size() + 1);
            for (EndPointHit e : returnedEndpoints) {
                if (!e.getIp().equals(dto.getIp())) {
                    viewStats1.setUniqueHits(viewStats1.getUniqueHits() + 1);
                    viewStatsRepository.save(viewStats1);
                    break;
                }
            }
            viewStatsRepository.save(viewStats1);
        }
        return EndPointHitMapper.toDto(endPointHit);
    }

    @Override
    public Collection<EndPointHitDto> getStats(String start, String end, String[] uris, Boolean unique) {
        List<EndPointHit> endPointHits = statRepository.findAllByUri(start, end, uris, unique);
        /*List<ViewStats> viewStats = viewStatsRepository.findViewStatsByUri(start, end, uris, unique);
        if (endPointHits.isEmpty()) {
            return Collections.emptyList();
        } else {

        }*/
        return endPointHits.stream()
                .map(EndPointHitMapper::toDto)
                .collect(Collectors.toList());
    }
}
