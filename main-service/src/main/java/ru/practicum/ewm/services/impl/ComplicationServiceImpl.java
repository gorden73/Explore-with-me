package ru.practicum.ewm.services.impl;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.ewm.models.Complication;
import ru.practicum.ewm.models.Event;
import ru.practicum.ewm.models.dto.complications.ComplicationDto;
import ru.practicum.ewm.models.dto.complications.NewComplicationDto;
import ru.practicum.ewm.models.dto.mappers.ComplicationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.repositories.ComplicationRepository;
import ru.practicum.ewm.repositories.EventRepository;
import ru.practicum.ewm.services.ComplicationService;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Service
@Slf4j
public class ComplicationServiceImpl implements ComplicationService {
    private final ComplicationRepository complicationRepository;
    private final EventRepository eventRepository;

    @Autowired
    public ComplicationServiceImpl(ComplicationRepository complicationRepository, EventRepository eventRepository) {
        this.complicationRepository = complicationRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    public Collection<ComplicationDto> getAllComplications(Boolean pinned, int from, int size) {
        Pageable page = PageRequest.of(from, size);
        log.info("Запрошены все подборки с {} в размере {} со статусом pinned = {}.", from, size, pinned);
        return ComplicationMapper.toDtoCollection(complicationRepository.findAll(page).getContent());
    }

    @Override
    public ComplicationDto getComplicationById(int id) {
        log.info("Запрошена подборка id{}.", id);
        return ComplicationMapper.toDto(complicationRepository.findById(id).orElseThrow());
    }

    @Override
    public ComplicationDto addComplication(NewComplicationDto complicationDto) {
        Complication complication = ComplicationMapper.toComplication(complicationDto);
        Set<Integer> eventsId = complicationDto.getEvents();
        Set<Event> events = new HashSet<>();
        for(Integer id : eventsId) {
            events.add(eventRepository.findById(id).orElseThrow());// надо доделать исключение
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
        Complication complication = complicationRepository.findById(compId).orElseThrow(); // добавить исключение
        Event event = eventRepository.findById(eventId).orElseThrow(); // добавить исключение
        complication.getEvents().remove(event);
        complicationRepository.save(complication);
    }

    @Override
    public void addEventToComplication(int compId, int eventId) {
       Complication complication = complicationRepository.findById(compId).orElseThrow(); // добавить исключение
       Event event = eventRepository.findById(eventId).orElseThrow(); // добавить исключение
       complication.getEvents().add(event);
       complicationRepository.save(complication);
    }

    @Override
    public void unpinComplicationAtMainPage(int compId) {
        Complication complication = complicationRepository.findById(compId).orElseThrow(); // добавить исключение
        complication.setPinned(false);
        complicationRepository.save(complication);
    }

    @Override
    public void pinComplicationAtMainPage(int compId) {
        Complication complication = complicationRepository.findById(compId).orElseThrow(); // добавить исключение
        complication.setPinned(true);
        complicationRepository.save(complication);
    }
}
