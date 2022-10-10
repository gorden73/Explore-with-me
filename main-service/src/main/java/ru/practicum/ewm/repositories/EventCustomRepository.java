package ru.practicum.ewm.repositories;

import org.springframework.stereotype.Repository;
import ru.practicum.ewm.models.Event;
import ru.practicum.ewm.models.EventState;
import ru.practicum.ewm.models.FilterCollector;

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
     * @param filterCollector объект, описывающий условия фильтрации
     * @return список событий, подходящих под переданные условия
     * @since 1.0
     */
    List<Event> getAllEvents(FilterCollector filterCollector);

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
