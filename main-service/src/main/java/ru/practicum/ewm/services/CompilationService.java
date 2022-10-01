package ru.practicum.ewm.services;

import ru.practicum.ewm.apis.admins.dtos.compilations.NewCompilationDto;
import ru.practicum.ewm.models.Compilation;
import ru.practicum.ewm.models.dtos.compilations.CompilationDto;

import java.util.Collection;

/**
 * Интерфейс сервиса для работы с подборками событий
 *
 * @see Compilation
 * @see CompilationDto
 * @since 1.0
 */
public interface CompilationService {
    /**
     * Метод позволяет получить коллекцию Dto подборок событий, подходящих под заданные условия
     *
     * @param pinned закреплена ли подборка на главной странце
     * @param from   количество подборок, которое надо пропустить для формирования коллекции
     * @param size   количество подборок в коллекции
     * @return коллекция Dto подборок событий
     * @since 1.0
     */
    Collection<CompilationDto> getAllCompilations(Boolean pinned, int from, int size);

    /**
     * Метод позволяет получить Dto подборки событий по идентификатору
     *
     * @param id идентификатор подборки
     * @return Dto подборки событий
     * @since 1.0
     */
    CompilationDto getCompilationDtoById(int id);

    /**
     * Метод позволяет получить подборку событий по идентификатору
     *
     * @param id идентификатор подборки
     * @return подборка событий
     * @since 1.0
     */
    Compilation getCompilationById(int id);

    /**
     * Метод позволяет создать новую подборку событий
     *
     * @param compilationDto объект, описывающий основые свойства подборки событий, которые задает администратор
     * @return созданный объект, описывающий основные и дополнительные свойства подборки событий
     * @since 1.0
     */
    CompilationDto addCompilation(NewCompilationDto compilationDto);

    /**
     * Метод позволяет удалить имеющуюся подборку событий по идентификатору
     *
     * @param id идентификатор подборки событий
     * @since 1.0
     */
    void removeCompilationById(int id);

    /**
     * Метод позволяет удалить событие из подборки по идентификаторам подборки и события
     *
     * @param compId  идентификатор подборки событий
     * @param eventId идентификатор события
     * @since 1.0
     */
    void removeEventFromCompilation(int compId, int eventId);

    /**
     * Метод позволяет добавить событие в подборку по идентификаторам подборки и события
     *
     * @param compId  идентификатор подборки событий
     * @param eventId идентификатор события
     * @since 1.0
     */
    void addEventToCompilation(int compId, int eventId);

    /**
     * Метод позволяет открепить подборку по идентификатору от главной страницы
     *
     * @param compId идентификатор подборки событий
     * @since 1.0
     */
    void unpinCompilationAtMainPage(int compId);

    /**
     * Метод позволяет закрепить подборку по идентификатору на главной странице
     *
     * @param compId идентификатор подборки событий
     * @since 1.0
     */
    void pinCompilationAtMainPage(int compId);
}
