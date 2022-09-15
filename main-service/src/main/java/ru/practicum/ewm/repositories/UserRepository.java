package ru.practicum.ewm.repositories;

import ru.practicum.ewm.models.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @Query(value = "select u " +
            "from User u " +
            "where u.id in ?1")
    List<User> getAllUsers(Integer[] ids);
}
