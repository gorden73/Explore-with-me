package ru.practicum.ewm.repositories;

import org.springframework.stereotype.Repository;
import ru.practicum.ewm.models.Event;
import ru.practicum.ewm.models.EventState;

import java.util.List;

/**
 * Кастомный интерфейс для работы с репозиторием событий
 *
 * @since 1.0
 */
@Repository
public interface EventCustomRepository {
    /**
     * Метод позволяет получить список событий, подходящих под переданные условия
     *
     * @param text          текст для поиска в содержимом аннотации и подробном описании события
     * @param categories    список идентификаторов категорий, в которых будет вестись поиск
     * @param paid          поиск только платных/бесплатных событий
     * @param rangeStart    дата и время, не раньше которых должно произойти событие
     * @param rangeEnd      дата и время, не позже которых должно произойти событие
     * @param onlyAvailable дата и время не позже которых должно произойти событие (по умолчанию false)
     * @param sort          Вариант сортировки: по дате события (EVENT_DATE) или по количеству просмотров (VIEWS)
     * @param from          количество событий, которые нужно пропустить для формирования текущего набора (по умолчанию 0)
     * @param size          количество событий в наборе (по умолчанию 10)
     * @return список событий, подходящих под переданные условия
     * @since 1.0
     */
    List<Event> getAllEvents(String text, Integer[] categories, boolean paid, String rangeStart,
                             String rangeEnd, boolean onlyAvailable, String sort, int from, int size);

    /**
     * Метод позволяет найти события, подходящие под переданные условия
     *
     * @param users      список идентификаторов пользователей, события которых нужно найти
     * @param states     список статусов, в которых находятся искомые события {@link EventState}
     * @param categories список идентификаторов категорий, в которых нужно вести поиск
     * @param rangeStart дата и время, не раньше которых должно произойти событие
     * @param rangeEnd   дата и время, не позже которых должно произойти событие
     * @param from       количество событий, которые нужно пропустить для формирования текущего набора(по умолчанию 0)
     * @param size       количество событий в наборе(по умолчанию 10)
     * @return список событий, подходящих под переданные условия
     * @since 1.0
     */
    List<Event> searchEventsToAdmin(Integer[] users, EventState[] states, Integer[] categories, String rangeStart,
                                    String rangeEnd, int from, int size);
}
