package ru.practicum.stat.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.stat.models.EndPointHit;
import ru.practicum.stat.models.dto.EndPointHitDto;
import ru.practicum.stat.models.dto.ViewStats;
import ru.practicum.stat.models.dto.mappers.EndPointHitMapper;
import ru.practicum.stat.repositories.StatRepository;
import ru.practicum.stat.repositories.ViewStatsRepository;
import ru.practicum.stat.services.StatService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

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
    public Collection<ViewStats> getStats(long start, long end, String[] uris, Boolean unique) {
        if (unique != null) {
            if (unique.equals(true)) {
                if (uris != null) {
                    if (uris.length != 0) {
                        List<EndPointHit> endPointHits = statRepository.getUniqueStats(start, end, uris);
                        List<ViewStats> viewStatsList = new ArrayList<>();
                        for(EndPointHit hit : endPointHits) {
                            String uri = hit.getUri();
                            ViewStats viewStats = new ViewStats(hit.getApp(), uri, statRepository.getUriHits(uri));
                            viewStatsList.add(viewStats);
                        }
                        return viewStatsList;
                    }
                } else {
                    List<EndPointHit> endPointHits = statRepository.getUniqueStatsWithoutUris(start, end);
                }
            }
        }
        if (uris != null) {
            if (uris.length != 0) {
                List<EndPointHit> endPointHits = statRepository.getStats(start, end, uris);
            }
        } else {
            List<EndPointHit> endPointHits = statRepository.getStatsWithoutUris(start, end);
        }
        return null;
    }
}
