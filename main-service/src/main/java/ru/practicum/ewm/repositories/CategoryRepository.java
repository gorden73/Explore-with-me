package ru.practicum.ewm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.models.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
