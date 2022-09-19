package ru.practicum.ewm.repositories.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.models.Event;
import ru.practicum.ewm.models.EventState;
import ru.practicum.ewm.repositories.EventRepositoryCustom;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class EventRepositoryCustomImpl implements EventRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    /*@Override
    public Page getAllEvents(String text, Integer[] categories, boolean paid,
                             String rangeStart, String rangeEnd,
                             boolean onlyAvailable, EventState state, Pageable page) {
        TypedQuery q = entityManager.createQuery(query);
        q.setFirstResult(page.getPageNumber() * page.getPageSize());
        q.setMaxResults(page.getPageSize());
        return new PageImpl<>(q.getResultList(), page, findAllEvents(text, categories, paid, rangeStart, rangeEnd,
                onlyAvailable, state));
        *//*return findAllEvents(text, categories, paid, rangeStart, rangeEnd,
                onlyAvailable, state);*//*
    }*/

    @Override
    public List<Event> getAllEvents(String text, Integer[] categories, boolean paid,
                                     String rangeStart, String rangeEnd,
                                     boolean onlyAvailable, /*EventState state, */Pageable page) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery query = cb.createQuery(Event.class);
        Root eventRoot = query.from(Event.class);
        List<Predicate> filterPredicates = new ArrayList<>();
        if (text != null && !text.isEmpty() && !text.isBlank()) {
            filterPredicates.add(cb.like(eventRoot.get("annotation"), text.toUpperCase()));
            filterPredicates.add(cb.like(eventRoot.get("description"), text.toUpperCase()));
        }
        if (categories != null && categories.length != 0) {
            filterPredicates.add(cb.isTrue(eventRoot.get("category").in(categories)));
        }
        if ((rangeStart != null || !rangeStart.isEmpty() || !rangeStart.isBlank())
                && (rangeEnd != null || !rangeEnd.isEmpty() || !rangeEnd.isBlank())) {
            filterPredicates.add(cb.between(eventRoot.get("eventDate"), LocalDateTime.parse(rangeStart),
                    LocalDateTime.parse(rangeEnd)));
        } else {
            filterPredicates.add(cb.greaterThan(eventRoot.get("eventDate"), cb.currentTimestamp()));
        }
        filterPredicates.add(cb.equal(eventRoot.get("isPaid"), paid));
        filterPredicates.add(cb.equal(eventRoot.get("isAvailable"), onlyAvailable));
        //filterPredicates.add(cb.equal(eventRoot.get("state"), state));
        query.select(cb.construct(Event.class, eventRoot)).where(cb.and(filterPredicates.toArray(new Predicate[0])));
        return entityManager.createQuery(query).getResultList();
    }
}
