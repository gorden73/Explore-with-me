package ru.practicum.ewm.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.errors.Error;
import ru.practicum.ewm.exceptions.ConflictException;
import ru.practicum.ewm.exceptions.NotFoundException;
import ru.practicum.ewm.models.Compilation;
import ru.practicum.ewm.models.Event;
import ru.practicum.ewm.models.dto.compilations.CompilationDto;
import ru.practicum.ewm.models.dto.compilations.NewCompilationDto;
import ru.practicum.ewm.models.dto.mappers.CompilationMapper;
import ru.practicum.ewm.repositories.CompilationRepository;
import ru.practicum.ewm.services.CompilationService;
import ru.practicum.ewm.services.EventService;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventService eventService;

    @Autowired
    public CompilationServiceImpl(CompilationRepository compilationRepository, EventService eventService) {
        this.compilationRepository = compilationRepository;
        this.eventService = eventService;
    }

    @Override
    public Collection<CompilationDto> getAllCompilations(Boolean pinned, int from, int size) {
        Pageable page = PageRequest.of(from, size);
        if (pinned == null) {
            log.info("Запрошены все подборки начиная с {} в размере {}.", from, size);
            return CompilationMapper.toDtoCollection(compilationRepository.findAll(page).getContent());
        }
        log.info("Запрошены все подборки с {} в размере {} со статусом pinned = {}.", from, size, pinned);
        return CompilationMapper.toDtoCollection(compilationRepository.findAllByPinned(pinned, page));
    }

    @Override
    public CompilationDto getCompilationDtoById(int id) {
        return CompilationMapper.toDto(getCompilationById(id));
    }

    @Override
    public Compilation getCompilationById(int id) {
        log.info("Запрошена подборка id{}.", id);
        return compilationRepository.findById(id).orElseThrow(() ->
                new NotFoundException(List.of(
                        new Error("id", "неверное значение " + id).toString()),
                        "Невозможно получить подборку.",
                        String.format("Подборка с id%d не найдена.", id)));
    }

    @Override
    public CompilationDto addCompilation(NewCompilationDto compilationDto) {
        Compilation compilation = CompilationMapper.toCompilation(compilationDto);
        Set<Integer> eventsId = compilationDto.getEvents();
        Set<Event> events = new HashSet<>();
        for (Integer id : eventsId) {
            events.add(eventService.getEventById(id));
        }
        compilation.setEvents(events);
        Compilation savedCompilation = compilationRepository.save(compilation);
        log.info("Добавлена подборка id{}.", savedCompilation.getId());
        return CompilationMapper.toDto(savedCompilation);
    }

    @Override
    public void removeCompilationById(int id) {
        compilationRepository.deleteById(id);
        log.info("Удалена подборка id{}.", id);
    }

    @Override
    public void removeEventFromCompilation(int compId, int eventId) {
        Compilation compilation = getCompilationById(compId);
        Event event = eventService.getEventById(eventId);
        if (compilation.getEvents().contains(event)) {
            compilation.getEvents().remove(event);
            compilationRepository.save(compilation);
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
    public void addEventToCompilation(int compId, int eventId) {
        Compilation compilation = getCompilationById(compId);
        Event event = eventService.getEventById(eventId);
        if (!compilation.getEvents().contains(event)) {
            compilation.getEvents().add(event);
            compilationRepository.save(compilation);
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
    public void unpinCompilationAtMainPage(int compId) {
        Compilation compilation = getCompilationById(compId);
        if (compilation.isPinned()) {
            compilation.setPinned(false);
            compilationRepository.save(compilation);
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
    public void pinCompilationAtMainPage(int compId) {
        Compilation compilation = getCompilationById(compId);
        if (!compilation.isPinned()) {
        compilation.setPinned(true);
        compilationRepository.save(compilation);
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
