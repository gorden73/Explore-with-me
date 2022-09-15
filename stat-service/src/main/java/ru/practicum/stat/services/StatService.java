package ru.practicum.stat.services;

import ru.practicum.stat.models.dto.EndPointHitDto;
import ru.practicum.stat.models.dto.ViewStats;

import java.util.Collection;

public interface StatService {
    EndPointHitDto addEndPointHit(EndPointHitDto dto);

    Collection<ViewStats> getStats(long start, long end, String[] uris, Boolean unique);
}
