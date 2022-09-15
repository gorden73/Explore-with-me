package ru.practicum.stat.controllers;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.stat.models.dto.EndPointHitDto;
import ru.practicum.stat.models.dto.ViewStats;
import ru.practicum.stat.services.StatService;

import javax.validation.constraints.Positive;
import java.util.Collection;

@RestController
@Validated
public class StatController {
    private final StatService statService;

    @Autowired
    public StatController(StatService statService) {
        this.statService = statService;
    }

    @PostMapping("/hit")
    public EndPointHitDto addEndPointHit(@RequestBody EndPointHitDto dto) {
        return statService.addEndPointHit(dto);
    }

    @GetMapping("/stats")
    public Collection<ViewStats> getStats(@RequestParam @Positive long start,
                                             @RequestParam @Positive long end,
                                             @RequestParam(required = false) String[] uris,
                                             @RequestParam(required = false) Boolean unique) {
        return statService.getStats(start, end, uris, unique);
    }
}
