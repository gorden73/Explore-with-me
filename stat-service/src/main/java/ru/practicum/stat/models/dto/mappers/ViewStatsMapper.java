package ru.practicum.stat.models.dto.mappers;

import org.springframework.stereotype.Component;
import ru.practicum.stat.models.ViewStats;
import ru.practicum.stat.models.dto.ViewStatsDto;

@Component
public class ViewStatsMapper {
    public static ViewStats toViewStats(ViewStatsDto dto) {
        return new ViewStats(
                dto.getApp(),
                dto.getUri(),
                dto.getHits()
        );
    }

    public static ViewStatsDto toDto(ViewStats viewStats) {
        return new ViewStatsDto(
                viewStats.getApp(),
                viewStats.getUri(),
                viewStats.getHits()
        );
    }
}
