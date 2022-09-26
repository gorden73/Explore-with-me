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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
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
                    .map(user -> calculateRating(user))
                    .collect(Collectors.toList()));
        }
        log.info("Запрошен список пользователей {}.", (Object) ids);
        return UserMapper.toDtoCollection(userRepository.getAllUsers(ids));
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

    private void calculateRating(User user) {
        List<Event> userEvents = eventRepository.findEventsByInitiator(user, PageRequest.of(0, Integer.MAX_VALUE));
        List<Like> likes = new ArrayList<>();
        List<Like> dislikes = new ArrayList<>();
        for (Event event : userEvents) {
            Optional<Like> like = likeRepository.findByUserAndEventAndIsLike(user, event, true);
            like.ifPresent(likes::add);
            Optional<Like> dislike = likeRepository.findByUserAndEventAndIsLike(user, event, false);
            dislike.ifPresent(dislikes::add);
        }
        float rating;
        if (likes.size() > 0 && dislikes.size() == 0) {
            rating = 5;
        } else if ((dislikes.size() > 0 && likes.size() == 0) || (likes.size() == dislikes.size())) {
            rating = 0;
        } else {
            rating = likes.size() / dislikes.size();
        }
        user.setRating(rating);
    }
}
