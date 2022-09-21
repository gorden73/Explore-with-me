package ru.practicum.ewm.repositories.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Repository
public class EventCustomRepositoryImpl implements EventCustomRepository {
    private final EntityManager entityManager;

    @Autowired
    public EventCustomRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Event> getAllEvents(String text, Integer[] categories, boolean paid, String rangeStart, String rangeEnd,
                                    boolean onlyAvailable, Pageable page) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Event> query = cb.createQuery(Event.class);
        Root<Event> eventRoot = query.from(Event.class);
        List<Predicate> textPredicate = new ArrayList<>();
        List<Predicate> filterPredicates = new ArrayList<>();
        if (text != null && !text.isEmpty() && !text.isBlank()) {
            textPredicate.add(cb.like(eventRoot.get("annotation"), "%" + text.toUpperCase() + "%"));
            textPredicate.add(cb.like(eventRoot.get("description"), "%" + text.toUpperCase() + "%"));
        }
        if (categories != null && categories.length != 0) {
            filterPredicates.add(cb.isTrue(eventRoot.get("category").in(categories)));
        }
        if ((rangeStart != null || !rangeStart.isEmpty() || !rangeStart.isBlank())
                && (rangeEnd != null || !rangeEnd.isEmpty() || !rangeEnd.isBlank())) {
            filterPredicates.add(cb.between(eventRoot.get("eventDate"), LocalDateTime.parse(rangeStart,
                    DateTimeFormatter.ISO_LOCAL_DATE_TIME), LocalDateTime.parse(rangeEnd,
                    DateTimeFormatter.ISO_LOCAL_DATE_TIME)));
        } else {
            filterPredicates.add(cb.greaterThan(eventRoot.get("eventDate"), cb.currentTimestamp()));
        }
        filterPredicates.add(cb.equal(eventRoot.get("paid"), paid));
        if (onlyAvailable) {
            filterPredicates.add(cb.equal(eventRoot.get("isAvailable"), true));
        }
        query.select(eventRoot).where(cb.or(textPredicate.toArray((new Predicate[]{}))),
                cb.and(filterPredicates.toArray(new Predicate[]{})));
        TypedQuery<Event> typedQuery = entityManager.createQuery(query);
        return typedQuery.getResultList();
    }

    @Override
    public List<Event> searchEventsToAdmin(Integer[] users, EventState[] states, Integer[] categories,
                                           String rangeStart, String rangeEnd, Pageable page) {
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
        if ((rangeStart != null || !rangeStart.isEmpty() || !rangeStart.isBlank())
                && (rangeEnd != null || !rangeEnd.isEmpty() || !rangeEnd.isBlank())) {
            filterPredicates.add(cb.between(eventRoot.get("eventDate"), LocalDateTime.parse(rangeStart,
                    DateTimeFormatter.ISO_LOCAL_DATE_TIME), LocalDateTime.parse(rangeEnd,
                    DateTimeFormatter.ISO_LOCAL_DATE_TIME)));
        } else {
            filterPredicates.add(cb.greaterThan(eventRoot.get("eventDate"), cb.currentTimestamp()));
        }
        query.select(eventRoot).where(cb.and(filterPredicates.toArray(new Predicate[]{})));
        TypedQuery<Event> typedQuery = entityManager.createQuery(query);
        return typedQuery.getResultList();
    }
}
