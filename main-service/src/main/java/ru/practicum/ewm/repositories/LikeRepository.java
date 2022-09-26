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

    Optional<Like> findByUserAndEventAndIsLike(User user, Event event, Boolean IsLike);

    List<Like> findAllByEventAndIsLike(Event event, Boolean IsLike);
}
