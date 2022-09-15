package ru.practicum.stat.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.stat.models.EndPointHit;
import ru.practicum.stat.models.dto.ViewStats;

import java.util.List;
import java.util.Optional;

@Repository
public interface ViewStatsRepository extends JpaRepository<ViewStats, Integer> {
    Optional<ViewStats> findViewStatsByUri(String uri);

    @Query(value = "select hits " +
            "from ViewStats " +
            "where uri =?1")
    Integer getUriHits(String uri);
}
