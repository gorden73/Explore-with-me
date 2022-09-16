package ru.practicum.ewm.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.models.Complication;

import java.util.List;

@Repository
public interface ComplicationRepository extends JpaRepository<Complication, Integer> {
    List<Complication> findAllByPinned(Boolean pinned, Pageable page);
}
