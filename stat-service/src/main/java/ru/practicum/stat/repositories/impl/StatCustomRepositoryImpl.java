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

/**
 * Кастомный класс репозитория статистики, реазизующий интерфейс {@link StatCustomRepository}
 *
 * @since 1.0
 */
@Repository
public class StatCustomRepositoryImpl implements StatCustomRepository {
    /**
     * Менеджер сущностей
     *
     * @since 1.0
     */
    private final EntityManager entityManager;

    /**
     * Константа шаблона для форматирования даты и времени
     *
     * @since 1.0
     */
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    public StatCustomRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * Метод позволяет получить список записей данных о просмотрах эндпоинтов
     *
     * @param start  дата и время, не раньше которых должна быть добавлена информация о просмотрах эндпоинтов
     * @param end    дата и время, не раньше которых должна быть добавлена информация о просмотрах эндпоинтов
     * @param uri    URI, для которых надо предоставить статистику просмотров
     * @param unique выполнять поиск только для уникальных просмотров (по уникальным Ip-адресам)
     * @return список записей данных о просмотрах эндпоинтов
     * @since 1.0
     */
    @Override
    public List<EndPointHit> findAllByUri(String start, String end, String uri, Boolean unique) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<EndPointHit> query = cb.createQuery(EndPointHit.class);
        Root<EndPointHit> endpointRoot = query.from(EndPointHit.class);
        List<Predicate> filterPredicates = new ArrayList<>();
        filterPredicates.add(cb.between(endpointRoot.get("timestamp"), LocalDateTime.parse(start,
                StatCustomRepositoryImpl.FORMATTER), LocalDateTime.parse(end, StatCustomRepositoryImpl.FORMATTER)));
        if (uri != null && !uri.isEmpty() && !uri.isBlank()) {
            filterPredicates.add(cb.equal(endpointRoot.get("uri"), uri));
        }
        if (unique) {
            CriteriaBuilder uq = entityManager.getCriteriaBuilder();
            CriteriaQuery<String> uqQuery = uq.createQuery(String.class);
            Root<EndPointHit> uqRoot = uqQuery.from(EndPointHit.class);
            uqQuery.select(uqRoot.get("ip")).distinct(true).where(cb.equal(uqRoot.get("uri"), uri));
            TypedQuery<String> integerTypedQuery = entityManager.createQuery(uqQuery);
            List<String> uniqueIp = integerTypedQuery.getResultList();
            List<EndPointHit> uqResult = new ArrayList<>();
            for (String uqIp : uniqueIp) {
                query.select(endpointRoot).where(cb.and(filterPredicates.toArray(new Predicate[]{})),
                        cb.and(cb.equal(endpointRoot.get("ip"), uqIp)));
                uqResult.add(entityManager.createQuery(query).getResultList().get(0));
            }
            return uqResult;
        } else {
            query.select(endpointRoot).where(cb.and(filterPredicates.toArray(new Predicate[]{})));
        }
        TypedQuery<EndPointHit> typedQuery = entityManager.createQuery(query);
        return typedQuery.getResultList();
    }
}
