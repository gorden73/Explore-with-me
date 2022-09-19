package ru.practicum.stat.services.impl;

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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
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
        EndPointHit endPointHit = EndPointHitMapper.toEndPointHit(dto);

        //Integer hits = statRepository.getUriHits(endPointHit.getUri());
        /*ViewStats viewStats = new ViewStats(endPointHit.getApp(), endPointHit.getUri(), 0);
        Optional<ViewStats> viewStatsOpt = viewStatsRepository.findViewStatsByUri(endPointHit.getUri());
        if (viewStatsOpt.isEmpty()) {
            viewStatsRepository.save(viewStats);
        }
        viewStats.setHits(hits);
        viewStatsRepository.save(viewStats);*/
        return EndPointHitMapper.toDto(statRepository.save(endPointHit));
    }

    @Override
    public Collection<ViewStats> getStats(String start, String end, String[] uris, Boolean unique) {
        if (unique != null) {
            if (unique.equals(true)) {
                if (uris != null) {
                    if (uris.length != 0) {
                        List<EndPointHit> endPointHits = statRepository.getUniqueStats(LocalDateTime.parse(start),
                                LocalDateTime.parse(end),
                                uris);
                        List<ViewStats> viewStatsList = new ArrayList<>();
                        for(EndPointHit hit : endPointHits) {
                            String uri = hit.getUri();
                            ViewStats viewStats = new ViewStats(hit.getApp(), uri, statRepository.getUriHits(uri));
                            viewStatsList.add(viewStats);
                        }
                        return viewStatsList;
                    }
                } else {
                    List<EndPointHit> endPointHits = statRepository.getUniqueStatsWithoutUris(LocalDateTime.parse(start),
                            LocalDateTime.parse(end));
                }
            }
        }
        if (uris != null) {
            if (uris.length != 0) {
                List<EndPointHit> endPointHits = statRepository.getStats(LocalDateTime.parse(start),
                        LocalDateTime.parse(end), uris);
            }
        } else {
            List<EndPointHit> endPointHits = statRepository.getStatsWithoutUris(LocalDateTime.parse(start),
                    LocalDateTime.parse(end));
        }
        return null;
    }
}
