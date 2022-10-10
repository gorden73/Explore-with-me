package ru.practicum.stat.models.dto.mappers;

import org.springframework.stereotype.Component;
import ru.practicum.stat.models.ViewStats;
import ru.practicum.stat.models.dto.ViewStatsDto;

/**
 * Класс для работы со статистикой просмотров эндпоинта и преобразования сущности в Dto и обратно
 *
 * @see ViewStats
 * @see ViewStatsDto
 * @since 1.0
 */
@Component
public class ViewStatsMapper {

    /**
     * Метод позволяет преобразовать сущность статистики просмотров эндпоинта в Dto
     *
     * @param viewStats сущность статистики просмотров эндпоинта
     * @return Dto статистики просмотров эндпоинта
     * @since 1.0
     */
    public static ViewStatsDto toDto(ViewStats viewStats) {
        return new ViewStatsDto(
                viewStats.getApp(),
                viewStats.getUri(),
                viewStats.getHits()
        );
    }
}
