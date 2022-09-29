package ru.practicum.ewm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.models.Event;
import ru.practicum.ewm.models.Like;
import ru.practicum.ewm.models.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Integer> {
    Optional<Like> findByUserAndEventAndIsLikeIsTrue(User user, Event event);

    Optional<Like> findByUserAndEventAndIsLikeIsFalse(User user, Event event);

    List<Like> findAllByEventAndIsLikeIsTrue(Event event);

    List<Like> findAllByEventAndIsLikeIsFalse(Event event);
}
