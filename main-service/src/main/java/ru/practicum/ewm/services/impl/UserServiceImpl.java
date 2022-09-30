package ru.practicum.ewm.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.errors.Error;
import ru.practicum.ewm.exceptions.ConflictException;
import ru.practicum.ewm.models.Event;
import ru.practicum.ewm.models.Like;
import ru.practicum.ewm.models.User;
import ru.practicum.ewm.models.dto.mappers.UserMapper;
import ru.practicum.ewm.models.dto.users.UserDto;
import ru.practicum.ewm.repositories.EventRepository;
import ru.practicum.ewm.repositories.LikeRepository;
import ru.practicum.ewm.repositories.UserRepository;
import ru.practicum.ewm.services.UserService;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final EventRepository eventRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, LikeRepository likeRepository, EventRepository eventRepository) {
        this.userRepository = userRepository;
        this.likeRepository = likeRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    public Collection<UserDto> getAllUsers(Integer[] ids, int from, int size) {
        if (ids == null || ids.length == 0) {
            Pageable page = PageRequest.of(from, size);
            log.info("Запрошен список пользователей с {} в размере {}.", from, size);
            return UserMapper.toDtoCollection(userRepository.findAll(page).getContent().stream()
                    .peek(this::calculateUserEventsLikesAndDislikes)
                    .collect(Collectors.toList()));
        }
        log.info("Запрошен список пользователей {}.", (Object) ids);
        return UserMapper.toDtoCollection(userRepository.getAllUsers(ids).stream()
                .peek(this::calculateUserEventsLikesAndDislikes)
                .collect(Collectors.toList()));
    }

    @Override
    public UserDto addUser(UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        user.setRating(0f);
        log.info("Добавлен новый пользователь email {}.", user.getEmail());
        try {
            return UserMapper.toDto(userRepository.save(user));
        } catch (DataIntegrityViolationException e) {
            log.error("Пользователь с email '{}' уже существует.", user.getEmail());
            throw new ConflictException(List.of(
                    new Error("email", "должно быть уникальным").toString()),
                    "Невозможно создать пользователя.",
                    String.format("Пользователь с email '%s' уже существует.", user.getEmail()));
        }
    }

    @Override
    public void removeUser(int userId) {
        log.info("Удален пользователь id{}.", userId);
        userRepository.deleteById(userId);
    }

    /**
     * Метод позволяет рассчитать рейтинг пользователя на основе количества лайков и дизлайков его событий
     *
     * @param likes    количество лайков событиям пользователя
     * @param dislikes количество дизлайков событиям пользователя
     * @return рейтинг пользователя
     */
    private float calculateUserRating(float likes, float dislikes) {
        float rating;
        if (likes > 0 && dislikes == 0) {
            rating = 5;
        } else if ((dislikes > 0 && likes == 0) || (likes == dislikes)) {
            rating = 0;
        } else {
            rating = likes / dislikes;
        }
        return rating;
    }

    /**
     * Метод позволяет рассчитать количество лайков и дизлайков событиям пользователя
     *
     * @param user организатор событий
     */
    @Override
    public void calculateUserEventsLikesAndDislikes(User user) {
        List<Event> userEvents = eventRepository.findEventsByInitiator(user, PageRequest.of(0, Integer.MAX_VALUE));
        float likes = 0;
        float dislikes = 0;
        for (Event event : userEvents) {
            List<Like> likeList = likeRepository.findAllByEventAndIsLikeIsTrue(event);
            if (!likeList.isEmpty()) {
                likes = likes + likeList.size();
            }
            List<Like> dislikeList = likeRepository.findAllByEventAndIsLikeIsFalse(event);
            if (!dislikeList.isEmpty()) {
                dislikes = dislikes + dislikeList.size();
            }
        }
        user.setRating(calculateUserRating(likes, dislikes));
    }
}
