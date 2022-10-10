package ru.practicum.ewm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.models.Category;

/**
 * Интерфейс для работы с репозиторием категорий событий, наследующий {@link JpaRepository}
 *
 * @since 1.0
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
