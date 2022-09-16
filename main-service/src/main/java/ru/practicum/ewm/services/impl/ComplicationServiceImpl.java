package ru.practicum.ewm.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.errors.Error;
import ru.practicum.ewm.exceptions.ConflictException;
import ru.practicum.ewm.exceptions.ForbiddenException;
import ru.practicum.ewm.exceptions.NotFoundException;
import ru.practicum.ewm.models.Complication;
import ru.practicum.ewm.models.Event;
import ru.practicum.ewm.models.dto.complications.ComplicationDto;
import ru.practicum.ewm.models.dto.complications.NewComplicationDto;
import ru.practicum.ewm.models.dto.mappers.ComplicationMapper;
import ru.practicum.ewm.repositories.ComplicationRepository;
import ru.practicum.ewm.services.ComplicationService;
import ru.practicum.ewm.services.EventService;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class ComplicationServiceImpl implements ComplicationService {
    private final ComplicationRepository complicationRepository;
    private final EventService eventService;

    @Autowired
    public ComplicationServiceImpl(ComplicationRepository complicationRepository, EventService eventService) {
        this.complicationRepository = complicationRepository;
        this.eventService = eventService;
    }

    @Override
    public Collection<ComplicationDto> getAllComplications(Boolean pinned, int from, int size) {
        Pageable page = PageRequest.of(from, size);
        if (pinned == null) {
            log.info("Запрошены все подборки начиная с {} в размере {}.", from, size);
            return ComplicationMapper.toDtoCollection(complicationRepository.findAll(page).getContent());
        }
        log.info("Запрошены все подборки с {} в размере {} со статусом pinned = {}.", from, size, pinned);
        return ComplicationMapper.toDtoCollection(complicationRepository.findAllByPinned(pinned, page));
    }

    @Override
    public ComplicationDto getComplicationDtoById(int id) {
        return ComplicationMapper.toDto(getComplicationById(id));
    }

    @Override
    public Complication getComplicationById(int id) {
        log.info("Запрошена подборка id{}.", id);
        return complicationRepository.findById(id).orElseThrow(() ->
                new NotFoundException(List.of(
                        new Error("id", "неверное значение " + id).toString()),
                        "Невозможно получить подборку.",
                        String.format("Подборка с id%d не найдена.", id)));
    }

    @Override
    public ComplicationDto addComplication(NewComplicationDto complicationDto) {
        Complication complication = ComplicationMapper.toComplication(complicationDto);
        Set<Integer> eventsId = complicationDto.getEvents();
        Set<Event> events = new HashSet<>();
        for (Integer id : eventsId) {
            events.add(eventService.getEventById(id));
        }
        complication.setEvents(events);
        Complication savedComplication = complicationRepository.save(complication);
        log.info("Добавлена подборка id{}.", savedComplication.getId());
        return ComplicationMapper.toDto(savedComplication);
    }

    @Override
    public void removeComplicationById(int id) {
        complicationRepository.deleteById(id);
        log.info("Удалена подборка id{}.", id);
    }

    @Override
    public void removeEventFromComplication(int compId, int eventId) {
        Complication complication = getComplicationById(compId);
        Event event = eventService.getEventById(eventId);
        if (complication.getEvents().contains(event)) {
            complication.getEvents().remove(event);
            complicationRepository.save(complication);
            log.info("Удалено событие id{} из подборки id{}.", eventId, compId);
        } else {
            log.error("В подборке id{} не найдено событие id{}.", compId, eventId);
            throw new NotFoundException(List.of(
                    new Error("id", "неверное значение " + eventId).toString()),
                    "Невозможно удалить событие из подборки.",
                    String.format("Событие с id%d не найдено в подборке id%d.", eventId, compId));
        }
    }

    @Override
    public void addEventToComplication(int compId, int eventId) {
        Complication complication = getComplicationById(compId);
        Event event = eventService.getEventById(eventId);
        if (!complication.getEvents().contains(event)) {
            complication.getEvents().add(event);
            complicationRepository.save(complication);
            log.info(String.format("Добавлено событие id%d в подборку id%d.", eventId, compId));
        } else {
            log.error("В подборке id{} уже есть событие id{}.", compId, eventId);
            throw new ConflictException(List.of(
                    new Error("id", "неверное значение " + eventId).toString()),
                    "Невозможно добавить событие в подборки.",
                    String.format("Событие с id%d уже есть в подборке id%d.", eventId, compId));
        }
    }

    @Override
    public void unpinComplicationAtMainPage(int compId) {
        Complication complication = getComplicationById(compId);
        if (complication.isPinned()) {
            complication.setPinned(false);
            complicationRepository.save(complication);
            log.info(String.format("Откреплена от главной странице подборка id%d.", compId));
        } else {
            log.error("Нельзя открепить не закрепленную на главной странице подборку id{}.", compId);
            throw new ConflictException(List.of(
                    new Error("id", "неверное значение " + compId).toString()),
                    "Нельзя открепить не закрепленную на главной странице подборку.",
                    String.format("Подборка id%d не закреплена на главной странице.", compId));
        }
    }

    @Override
    public void pinComplicationAtMainPage(int compId) {
        Complication complication = getComplicationById(compId);
        if (!complication.isPinned()) {
        complication.setPinned(true);
        complicationRepository.save(complication);
        log.info(String.format("Закреплена на главной странице подборка id%d.", compId));
        } else {
            log.error("Нельзя закрепить закрепленную на главной странице подборку id{}.", compId);
            throw new ConflictException(List.of(
                    new Error("id", "неверное значение " + compId).toString()),
                    "Нельзя закрепить закрепленную на главной странице подборку.",
                    String.format("Подборка id%d уже закреплена на главной странице.", compId));
        }
    }
}
