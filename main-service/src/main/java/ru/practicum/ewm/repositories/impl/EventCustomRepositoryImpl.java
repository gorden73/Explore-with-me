package ru.practicum.ewm.repositories.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.controllers.apis.authorizedusers.dtos.mappers.EventMapper;
import ru.practicum.ewm.models.Event;
import ru.practicum.ewm.models.EventState;
import ru.practicum.ewm.repositories.EventCustomRepository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Кастомный класс, наследующий {@link EventCustomRepository}, для работы с репозиторием событий
 *
 * @since 1.0
 */
@Repository
public class EventCustomRepositoryImpl implements EventCustomRepository {
    /**
     * Менеджер сущностей
     *
     * @since 1.0
     */
    private final EntityManager entityManager;

    @Autowired
    public EventCustomRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

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
    @Override
    public List<Event> getAllEvents(String text, Integer[] categories, boolean paid, String rangeStart, String rangeEnd,
                                    boolean onlyAvailable, String sort, int from, int size) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Event> query = cb.createQuery(Event.class);
        Root<Event> eventRoot = query.from(Event.class);
        List<Predicate> textPredicate = new ArrayList<>();
        List<Predicate> filterPredicates = new ArrayList<>();
        if (text != null && !text.isEmpty() && !text.isBlank()) {
            textPredicate.add(cb.like(cb.lower(eventRoot.get("annotation")), "%" + text.toLowerCase() + "%"));
            textPredicate.add(cb.like(cb.lower(eventRoot.get("description")), "%" + text.toLowerCase() + "%"));
        }
        if (categories != null && categories.length != 0) {
            filterPredicates.add(cb.isTrue(eventRoot.get("category").in(categories)));
        }
        if (rangeStart != null && rangeEnd != null) {
            if (!rangeStart.isBlank() && !rangeEnd.isBlank()) {
                filterPredicates.add(cb.between(eventRoot.get("eventDate"), LocalDateTime.parse(rangeStart,
                        EventMapper.FORMATTER), LocalDateTime.parse(rangeEnd, EventMapper.FORMATTER)));
            }
        } else {
            filterPredicates.add(cb.greaterThan(eventRoot.get("eventDate"), cb.currentTimestamp()));
        }
        filterPredicates.add(cb.equal(eventRoot.get("paid"), paid));
        if (onlyAvailable) {
            filterPredicates.add(cb.equal(eventRoot.get("isAvailable"), true));
        }
        query.select(eventRoot).where(cb.or(textPredicate.toArray((new Predicate[]{}))),
                cb.and(filterPredicates.toArray(new Predicate[]{})));
        if (sort.equals("EVENT_DATE")) {
            query.orderBy(cb.desc(eventRoot.get("eventDate")));
        }
        TypedQuery<Event> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult(from);
        typedQuery.setMaxResults(size);
        return typedQuery.getResultList();
    }

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
    @Override
    public List<Event> searchEventsToAdmin(Integer[] users, EventState[] states, Integer[] categories,
                                           String rangeStart, String rangeEnd, int from, int size) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Event> query = cb.createQuery(Event.class);
        Root<Event> eventRoot = query.from(Event.class);
        List<Predicate> filterPredicates = new ArrayList<>();
        if (users != null && users.length != 0) {
            filterPredicates.add(cb.isTrue(eventRoot.get("initiator").in(users)));
        }
        if (states != null && states.length != 0) {
            filterPredicates.add(cb.isTrue(eventRoot.get("state").in(states)));
        }
        if (categories != null && categories.length != 0) {
            filterPredicates.add(cb.isTrue(eventRoot.get("category").in(categories)));
        }
        if (rangeStart != null && rangeEnd != null) {
            if (!rangeStart.isBlank() && !rangeEnd.isBlank()) {
                filterPredicates.add(cb.between(eventRoot.get("eventDate"), LocalDateTime.parse(rangeStart,
                        EventMapper.FORMATTER), LocalDateTime.parse(rangeEnd, EventMapper.FORMATTER)));
            }
        } else {
            filterPredicates.add(cb.greaterThan(eventRoot.get("eventDate"), cb.currentTimestamp()));
        }
        query.select(eventRoot).where(cb.and(filterPredicates.toArray(new Predicate[]{})));
        TypedQuery<Event> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult(from);
        typedQuery.setMaxResults(size);
        return typedQuery.getResultList();
    }
}
