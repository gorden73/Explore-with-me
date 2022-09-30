package ru.practicum.ewm.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.models.Compilation;

import java.util.List;

/**
 * Интерфейс для работы с репозиторием подборки событий, наследующий {@link JpaRepository}
 *
 * @since 1.0
 */
@Repository
public interface CompilationRepository extends JpaRepository<Compilation, Integer> {
    /**
     * Метод позволяет получить список подборок событий по заданному условию
     *
     * @param pinned закреплена ли подборка на главной странице
     * @param page номер страницы с подборками
     * @return список подборок событий по заданному условию
     * @since 1.0
     */
    List<Compilation> findAllByPinned(Boolean pinned, Pageable page);
}
