package ru.practicum.stat.repositories.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.practicum.stat.models.EndPointHit;
import ru.practicum.stat.repositories.StatCustomRepository;

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
public class StatCustomRepositoryImpl implements StatCustomRepository {
    private final EntityManager entityManager;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    public StatCustomRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<EndPointHit> findAllByUri(String start, String end, String[] uris, Boolean unique) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<EndPointHit> query = cb.createQuery(EndPointHit.class);
        Root<EndPointHit> endpointRoot = query.from(EndPointHit.class);
        List<Predicate> filterPredicates = new ArrayList<>();
        if ((start != null || !start.isEmpty() || !start.isBlank())
                && (end != null || !end.isEmpty() || !end.isBlank())) {
            filterPredicates.add(cb.between(endpointRoot.get("timestamp"), LocalDateTime.parse(start,
                    StatCustomRepositoryImpl.FORMATTER), LocalDateTime.parse(end, StatCustomRepositoryImpl.FORMATTER)));
        }
        if (uris != null && uris.length != 0) {
            filterPredicates.add(cb.isTrue(endpointRoot.get("uri").in(uris)));
        }
        /*if (unique) {
            filterPredicates.add(cb.equal(endpointRoot.get("ip"), true));
        }*/
        query.select(endpointRoot).where(cb.and(filterPredicates.toArray(new Predicate[]{})));
        TypedQuery<EndPointHit> typedQuery = entityManager.createQuery(query);
        return typedQuery.getResultList();
    }
}
