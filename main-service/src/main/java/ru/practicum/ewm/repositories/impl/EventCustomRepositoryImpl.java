package ru.practicum.ewm.repositories.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.controllers.apis.authorizedusers.dtos.mappers.EventMapper;
import ru.practicum.ewm.models.Event;
import ru.practicum.ewm.models.EventSortType;
import ru.practicum.ewm.models.EventState;
import ru.practicum.ewm.models.FilterCollector;
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

    @Override
    public List<Event> getAllEvents(FilterCollector filterCollector) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Event> query = cb.createQuery(Event.class);
        Root<Event> eventRoot = query.from(Event.class);
        List<Predicate> textPredicate = new ArrayList<>();
        List<Predicate> filterPredicates = new ArrayList<>();
        if (filterCollector.getText() != null && !filterCollector.getText().isEmpty()
                && !filterCollector.getText().isBlank()) {
            textPredicate.add(cb.like(cb.lower(eventRoot.get("annotation")), "%" +
                    filterCollector.getText().toLowerCase() + "%"));
            textPredicate.add(cb.like(cb.lower(eventRoot.get("description")), "%" +
                    filterCollector.getText().toLowerCase() + "%"));
        }
        if (filterCollector.getCategories() != null && filterCollector.getCategories().length != 0) {
            filterPredicates.add(cb.isTrue(eventRoot.get("category").in(filterCollector.getCategories())));
        }
        if (filterCollector.getRangeStart() != null && filterCollector.getRangeEnd() != null) {
            if (!filterCollector.getRangeStart().isBlank() && !filterCollector.getRangeEnd().isBlank()) {
                filterPredicates.add(cb.between(eventRoot.get("eventDate"),
                        LocalDateTime.parse(filterCollector.getRangeStart(), EventMapper.FORMATTER),
                        LocalDateTime.parse(filterCollector.getRangeEnd(), EventMapper.FORMATTER)));
            }
        } else {
            filterPredicates.add(cb.greaterThan(eventRoot.get("eventDate"), cb.currentTimestamp()));
        }
        filterPredicates.add(cb.equal(eventRoot.get("paid"), filterCollector.getPaid()));
        if (filterCollector.isOnlyAvailable()) {
            filterPredicates.add(cb.equal(eventRoot.get("isAvailable"), true));
        }
        query.select(eventRoot).where(cb.or(textPredicate.toArray((new Predicate[]{}))),
                cb.and(filterPredicates.toArray(new Predicate[]{})));
        if (EventSortType.EVENT_DATE.toString().equals(filterCollector.getSort())) {
            query.orderBy(cb.desc(eventRoot.get("eventDate")));
        }
        TypedQuery<Event> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult(filterCollector.getFrom());
        typedQuery.setMaxResults(filterCollector.getSize());
        return typedQuery.getResultList();
    }

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
