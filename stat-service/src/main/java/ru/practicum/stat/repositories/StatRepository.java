package ru.practicum.stat.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.stat.models.EndPointHit;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatRepository extends JpaRepository<EndPointHit, Integer>, StatCustomRepository {

    List<EndPointHit> findAllByUri (String uri);


    @Query(value = "select count(id) " +
            "from EndPointHit " +
            "where uri = ?1")
    Integer getUriHits(String uri);

    @Query(value = "select app, uri, count(id) " +
            "from stats " +
            "where timestamp > ?1 " +
            "and timestamp < ?2 " +
            "and uri in ?3 " +
            "group by uri, ip, app", nativeQuery = true)
    List<EndPointHit> getUniqueStats(LocalDateTime start, LocalDateTime end, String[] uris);

    @Query(value = "select app, uri, count(id) " +
            "from EndPointHit " +
            "where timestamp > ?1 " +
            "and timestamp < ?2 " +
            "group by ip")
    List<EndPointHit> getUniqueStatsWithoutUris(LocalDateTime start, LocalDateTime end);

    @Query(value = "select app, uri, count(id) " +
            "from EndPointHit " +
            "where timestamp > ?1 " +
            "and timestamp < ?2 " +
            "and uri in ?3")
    List<EndPointHit> getStats(LocalDateTime start, LocalDateTime end, String[] uris);

    @Query(value = "select app, uri, count(id) " +
            "from EndPointHit " +
            "where timestamp > ?1 " +
            "and timestamp < ?2")
    List<EndPointHit> getStatsWithoutUris(LocalDateTime start, LocalDateTime end);

}
