package ru.practicum.stat.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.stat.models.dto.EndPointHitDto;
import ru.practicum.stat.services.StatService;

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
    public Collection<EndPointHitDto> getStats(@RequestParam String start,
                                               @RequestParam String end,
                                               @RequestParam(required = false) String[] uris,
                                               @RequestParam(defaultValue = "false") Boolean unique) {
        return statService.getStats(start, end, uris, unique);
    }
}
