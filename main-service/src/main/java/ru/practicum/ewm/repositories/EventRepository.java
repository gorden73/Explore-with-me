package ru.practicum.ewm.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.models.Category;
import ru.practicum.ewm.models.Event;
import ru.practicum.ewm.models.User;

import java.util.List;
import java.util.Optional;

/**
 * Интерфейс для работы с репозиторием категорий событий, наследующий {@link JpaRepository} и
 * {@link EventCustomRepository}
 *
 * @since 1.0
 */
@Repository
public interface EventRepository extends JpaRepository<Event, Integer>, EventCustomRepository {

    /**
     * Метод позволяет получить страницу со списком событий пользователя
     *
     * @param initiator организатор события
     * @param page      номер страницы со списком событий пользователя
     * @return страница со списком событий пользователя
     * @since 1.0
     */
    @Query(value = "select e " +
            "from Event e " +
            "where e.initiator = ?1")
    List<Event> findEventsByInitiator(User initiator, Pageable page);

    /**
     * Метод позволяет получить событие по идентификатору и организатору
     *
     * @param id        идентификатор события
     * @param initiator организатор события
     * @return событие или null (если такое событие не найдено)
     * @since 1.0
     */
    Optional<Event> findEventByIdAndInitiator(int id, User initiator);

    /**
     * Метод позволяет получить список идентификаторов событий определенной категории
     *
     * @param category категория событий
     * @return список идентификаторов событий определенной категории
     * @since 1.0
     */
    @Query(value = "select id " +
            "from Event " +
            "where category = ?1")
    List<Integer> findEventsByCategory(Category category);
}
