package ru.practicum.ewm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.models.Event;
import ru.practicum.ewm.models.Like;
import ru.practicum.ewm.models.User;

import java.util.List;
import java.util.Optional;

/**
 * Класс для работы с хранилищем лайков/дизлайков
 */
@Repository
public interface LikeRepository extends JpaRepository<Like, Integer> {
    /**
     * Метод позволяет получить лайк указанному событию указанным пользователем
     * @param user пользователь, поставивший лайк
     * @param event событие, которому поставили лайк
     * @return лайк указанному событию указанным пользователем, если лайк поставлен, и null, если лайк не найден
     */
    Optional<Like> findByUserAndEventAndIsLikeIsTrue(User user, Event event);

    /**
     * Метод позволяет получить дизлайк указанному событию указанным пользователем
     * @param user пользователь, поставивший дизлайк
     * @param event событие, которому поставили дизлайк
     * @return дизлайк указанному событию указанным пользователем, если дизлайк поставлен, и null, если дизлайк не
     * найден
     */
    Optional<Like> findByUserAndEventAndIsLikeIsFalse(User user, Event event);

    /**
     * Метод позволяет получить все лайки указанному событию
     * @param event событие, по которому ищем лайки
     * @return список всех лайков указанного события
     */
    List<Like> findAllByEventAndIsLikeIsTrue(Event event);

    /**
     * Метод позволяет получить все дизлайки указанному событию
     * @param event событие, по которому ищем дизлайки
     * @return список всех дизлайков указанного события
     */
    List<Like> findAllByEventAndIsLikeIsFalse(Event event);
}
