package ru.practicum.stat.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.stat.models.EndPointHit;

import java.util.List;

@Repository
public interface StatRepository extends JpaRepository<EndPointHit, Integer> {
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
    List<EndPointHit> getUniqueStats(long start, long end, String[] uris);

    @Query(value = "select app, uri, count(id) " +
            "from EndPointHit " +
            "where timestamp > ?1 " +
            "and timestamp < ?2 " +
            "group by ip")
    List<EndPointHit> getUniqueStatsWithoutUris(long start, long end);

    @Query(value = "select app, uri, count(id) " +
            "from EndPointHit " +
            "where timestamp > ?1 " +
            "and timestamp < ?2 " +
            "and uri in ?3")
    List<EndPointHit> getStats(long start, long end, String[] uris);

    @Query(value = "select app, uri, count(id) " +
            "from EndPointHit " +
            "where timestamp > ?1 " +
            "and timestamp < ?2")
    List<EndPointHit> getStatsWithoutUris(long start, long end);

}
