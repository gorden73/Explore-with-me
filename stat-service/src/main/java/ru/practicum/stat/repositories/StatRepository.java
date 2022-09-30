package ru.practicum.stat.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.stat.models.EndPointHit;

@Repository
public interface StatRepository extends JpaRepository<EndPointHit, Integer>, StatCustomRepository {

}
