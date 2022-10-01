package ru.practicum.ewm.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.apis.admins.dtos.compilations.NewCompilationDto;
import ru.practicum.ewm.apis.admins.dtos.mappers.CompilationMapper;
import ru.practicum.ewm.errors.Error;
import ru.practicum.ewm.exceptions.ConflictException;
import ru.practicum.ewm.exceptions.NotFoundException;
import ru.practicum.ewm.models.Compilation;
import ru.practicum.ewm.models.Event;
import ru.practicum.ewm.models.dtos.compilations.CompilationDto;
import ru.practicum.ewm.repositories.CompilationRepository;
import ru.practicum.ewm.services.CompilationService;
import ru.practicum.ewm.services.EventService;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Класс сервиса для работы с подборками событий, реализующий {@link CompilationService}
 *
 * @see CompilationDto
 * @see NewCompilationDto
 * @since 1.0
 */
@Service
@Slf4j
public class CompilationServiceImpl implements CompilationService {
    /**
     * Интерфейс для работы с репозиторием подборки событий, наследующий {@link JpaRepository}
     *
     * @since 1.0
     */
    private final CompilationRepository compilationRepository;

    /**
     * Интерфейс сервиса для работы с событиями
     *
     * @since 1.0
     */
    private final EventService eventService;

    @Autowired
    public CompilationServiceImpl(CompilationRepository compilationRepository, EventService eventService) {
        this.compilationRepository = compilationRepository;
        this.eventService = eventService;
    }

    /**
     * Метод позволяет получить коллекцию Dto подборок событий, подходящих под заданные условия
     *
     * @param pinned закреплена ли подборка на главной странце
     * @param from   количество подборок, которое надо пропустить для формирования коллекции
     * @param size   количество подборок в коллекции
     * @return коллекция Dto подборок событий
     * @since 1.0
     */
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

    /**
     * Метод позволяет получить Dto подборки событий по идентификатору
     *
     * @param id идентификатор подборки
     * @return Dto подборки событий
     * @since 1.0
     */
    @Override
    public CompilationDto getCompilationDtoById(int id) {
        return CompilationMapper.toDto(getCompilationById(id));
    }

    /**
     * Метод позволяет получить подборку событий по идентификатору
     *
     * @param id идентификатор подборки
     * @return подборка событий
     * @since 1.0
     */
    @Override
    public Compilation getCompilationById(int id) {
        log.info("Запрошена подборка id{}.", id);
        return compilationRepository.findById(id).orElseThrow(() ->
                new NotFoundException(List.of(
                        new Error("id", "неверное значение " + id).toString()),
                        "Невозможно получить подборку.",
                        String.format("Подборка с id%d не найдена.", id)));
    }

    /**
     * Метод позволяет создать новую подборку событий
     *
     * @param compilationDto объект, описывающий основые свойства подборки событий, которые задает администратор
     * @return созданный объект, описывающий основные и дополнительные свойства подборки событий
     * @since 1.0
     */
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

    /**
     * Метод позволяет удалить имеющуюся подборку событий по идентификатору
     *
     * @param id идентификатор подборки событий
     * @since 1.0
     */
    @Override
    public void removeCompilationById(int id) {
        compilationRepository.deleteById(id);
        log.info("Удалена подборка id{}.", id);
    }

    /**
     * Метод позволяет удалить событие из подборки по идентификаторам подборки и события
     *
     * @param compId  идентификатор подборки событий
     * @param eventId идентификатор события
     * @since 1.0
     */
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

    /**
     * Метод позволяет добавить событие в подборку по идентификаторам подборки и события
     *
     * @param compId  идентификатор подборки событий
     * @param eventId идентификатор события
     * @since 1.0
     */
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

    /**
     * Метод позволяет открепить подборку по идентификатору от главной страницы
     *
     * @param compId идентификатор подборки событий
     * @since 1.0
     */
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

    /**
     * Метод позволяет закрепить подборку по идентификатору на главной странице
     *
     * @param compId идентификатор подборки событий
     * @since 1.0
     */
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
