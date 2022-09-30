package ru.practicum.ewm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.models.User;

import java.util.List;

/**
 * Интерфейс для работы с репозиторием пользователей, наследующий {@link JpaRepository}
 *
 * @since 1.0
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    /**
     * Метод позволяет получить список пользователей, идентификаторы которых есть в передаваемом списке
     *
     * @param ids идентификаторы пользователей
     * @return список пользователей, подходящих под переданные условия
     * @since 1.0
     */
    @Query(value = "select u " +
            "from User u " +
            "where u.id in ?1")
    List<User> getAllUsers(Integer[] ids);
}
