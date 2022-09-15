package ru.practicum.ewm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.models.Complication;

@Repository
public interface ComplicationRepository extends JpaRepository<Complication, Integer> {
}
