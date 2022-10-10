package ru.practicum.stat.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.stat.models.EndPointHit;

/**
 * Интерфейс репозитория статистики, наследующий от {@link org.springframework.data.jpa.repository.JpaRepository} и
 * {@link StatCustomRepository}
 *
 * @since 1.0
 */
@Repository
public interface StatRepository extends JpaRepository<EndPointHit, Integer>, StatCustomRepository {
}
