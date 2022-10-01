package ru.practicum.stat.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.stat.models.dto.EndPointHitDto;
import ru.practicum.stat.models.dto.ViewStatsDto;
import ru.practicum.stat.services.StatService;

import java.util.Collection;

/**
 * Контроллер для работы со статистикой просмотров эндпоинтов
 *
 * @since 1.0
 */
@RestController
@Validated
public class StatController {
    /**
     * Интерфейс сервиса для работы со статистикой просмотров эндпоинтов
     *
     * @since 1.0
     */
    private final StatService statService;

    @Autowired
    public StatController(StatService statService) {
        this.statService = statService;
    }

    /**
     * Метод позволяет добавить информацию о просмотре определенного эндпоинта
     *
     * @param dto Dto записи данных о просмотрах эндпоинтов
     * @return Dto записи данных о просмотрах эндпоинтов
     * @since 1.0
     */
    @PostMapping("/hit")
    public EndPointHitDto addEndPointHit(@RequestBody EndPointHitDto dto) {
        return statService.addEndPointHit(dto);
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
    @GetMapping("/stats")
    public Collection<ViewStatsDto> getStats(@RequestParam String start,
                                             @RequestParam String end,
                                             @RequestParam(required = false) String[] uris,
                                             @RequestParam(defaultValue = "false") Boolean unique) {
        return statService.getStats(start, end, uris, unique);
    }
}
