package ru.practicum.ewm.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.errors.Error;
import ru.practicum.ewm.exceptions.ConflictException;
import ru.practicum.ewm.exceptions.ForbiddenException;
import ru.practicum.ewm.models.Event;
import ru.practicum.ewm.models.EventState;
import ru.practicum.ewm.models.Like;
import ru.practicum.ewm.models.User;
import ru.practicum.ewm.models.dto.events.EventShortDto;
import ru.practicum.ewm.models.dto.likes.AdminLikeDto;
import ru.practicum.ewm.models.dto.likes.LikeDto;
import ru.practicum.ewm.models.dto.mappers.EventMapper;
import ru.practicum.ewm.repositories.LikeRepository;
import ru.practicum.ewm.repositories.UserRepository;
import ru.practicum.ewm.services.EventService;
import ru.practicum.ewm.services.LikeService;
import ru.practicum.ewm.services.UserService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class LikeServiceImpl implements LikeService {
    private final EventService eventService;
    private final LikeRepository likeRepository;
    private final UserService userService;
    private final UserRepository userRepository;

    @Autowired
    public LikeServiceImpl(EventService eventService, LikeRepository likeRepository, UserService userService, UserRepository userRepository) {
        this.eventService = eventService;
        this.likeRepository = likeRepository;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    /**
     * Метод позволяет пользователю поставить лайк чужому событию
     *
     * @param userId  идентификатор пользователя
     * @param eventId идентификатор события
     * @return краткая информация о событии, которому пользователь поставил лайк
     */
    @Override
    public EventShortDto addLike(int userId, int eventId) {
        User user = userRepository.findById(userId).get();
        Event event = eventService.getEventById(eventId);
        if (!event.getState().equals(EventState.PUBLISHED)) {
            log.error("Нельзя поставить like неопубликованному событию id{}.", eventId);
            throw new ForbiddenException(List.of(
                    new Error("eventId", "неверное значение").toString()),
                    String.format("Невозможно поставить like событию id%d.", eventId),
                    String.format("Cобытие id%d ещё не опубликовано.", eventId));
        }
        if (userId == event.getInitiator().getId()) {
            log.error("Пользователь id{} не может поставить like своему событию id{}.", userId, eventId);
            throw new ForbiddenException(List.of(
                    new Error("userId", "неверное значение").toString()),
                    String.format("Невозможно поставить like событию id%d.", eventId),
                    String.format("Пользователь id%d не может поставить like своему событию id%d.", userId, eventId));
        }
        Optional<Like> like = likeRepository.findByUserAndEventAndIsLikeIsTrue(user, event);
        if (like.isPresent()) {
            log.error("Пользователь id{} уже поставил like событию id{}.", userId, eventId);
            throw new ConflictException(List.of(
                    new Error("eventId", "неверное значение").toString()),
                    String.format("Невозможно поставить like событию id%d.", eventId),
                    String.format("Пользователь id%d уже поставил like событию id%d.", userId, eventId));
        }
        Optional<Like> dislike = likeRepository.findByUserAndEventAndIsLikeIsFalse(user, event);
        if (dislike.isPresent()) {
            log.error("Пользователь id{} уже поставил dislike событию id{}.", userId, eventId);
            throw new ConflictException(List.of(
                    new Error("eventId", "неверное значение").toString()),
                    String.format("Невозможно поставить like событию id%d.", eventId),
                    String.format("Пользователь id%d уже поставил dislike событию id%d.", userId, eventId));
        }
        likeRepository.save(new Like(user, event, true));
        calculateEventLikesAndDislikes(event, event.getInitiator().getId());
        userService.calculateUserEventsLikesAndDislikes(event.getInitiator());
        log.info("Пользователь id{} поставил like событию id{}.", userId, eventId);
        return EventMapper.toEventDto(event);
    }

    /**
     * Метод позволяет получить краткую информацию по всем лайкам своего события
     *
     * @param eventId идентификатор события
     * @return краткая информация по всем лайкам указанного события
     */
    @Override
    public List<LikeDto> getEventLikesDto(Integer userId, int eventId) {
        return EventMapper.likesToDtoCollection(getEventLikes(userId, eventId));
    }

    /**
     * Метод позволяет получить подробную информацию по всем лайкам указанного события
     *
     * @param userId  идентификатор пользователя(для администратора равен null)
     * @param eventId идентификатор события
     * @return подробная информация по всем лайкам указанного события
     */
    @Override
    public List<AdminLikeDto> getEventAdminLikesDto(Integer userId, int eventId) {
        return EventMapper.likesToAdminDtoCollection(getEventLikes(userId, eventId).stream()
                .peek(like -> calculateEventLikesAndDislikes(like.getEvent(), like.getEvent().getInitiator().getId()))
                .peek(like -> userService.calculateUserEventsLikesAndDislikes(like.getUser()))
                .peek(like -> userService.calculateUserEventsLikesAndDislikes(like.getEvent().getInitiator()))
                .collect(Collectors.toList()));
    }

    /**
     * Метод позволяет получить подробную информацию по всем дизлайкам указанного события
     *
     * @param userId  идентификатор пользователя(для администратора равен null)
     * @param eventId идентификатор события
     * @return подробная информация по всем дизлайкам указанного события
     */
    @Override
    public List<AdminLikeDto> getEventAdminDislikesDto(Integer userId, int eventId) {
        return EventMapper.likesToAdminDtoCollection(getEventDislikes(userId, eventId).stream()
                .peek(like -> calculateEventLikesAndDislikes(like.getEvent(), like.getEvent().getInitiator().getId()))
                .peek(like -> userService.calculateUserEventsLikesAndDislikes(like.getUser()))
                .peek(like -> userService.calculateUserEventsLikesAndDislikes(like.getEvent().getInitiator()))
                .collect(Collectors.toList()));
    }

    /**
     * Метод позволяет пользователю получить все лайки своего события по идентификатору
     *
     * @param userId  идентификатор пользователя
     * @param eventId идентификатор события
     * @return все лайки события
     */
    private List<Like> getEventLikes(Integer userId, int eventId) {
        Event event = eventService.getEventById(eventId);
        if (!event.getState().equals(EventState.PUBLISHED)) {
            log.error("У неопубликованного события id{} не может быть лайков.", eventId);
            throw new ForbiddenException(List.of(
                    new Error("eventId", "неверное значение").toString()),
                    String.format("Невозможно получить лайки события id%d.", eventId),
                    String.format("У неопубликованного события id%d не может быть лайков.", eventId));
        }
        if (userId == null) {
            log.info("Администратор запросил все лайки события id{}.", eventId);
            return likeRepository.findAllByEventAndIsLikeIsTrue(event);
        }
        if (userId != event.getInitiator().getId()) {
            log.error("Пользователь id{} не является организатором события id{}.", userId, eventId);
            throw new ForbiddenException(List.of(
                    new Error("userId", "неверное значение").toString()),
                    String.format("Невозможно получить список лайков событию id%d.", eventId),
                    String.format("Пользователь id%d не является организатором события id%d.", userId, eventId));
        }
        log.info("Пользователь id{} запросил все лайки своего события id{}.", userId, eventId);
        return likeRepository.findAllByEventAndIsLikeIsTrue(event);
    }

    /**
     * Метод позволяет пользователю поставить дизлайк чужому событию
     *
     * @param userId  идентификатор пользователя
     * @param eventId идентификатор события
     * @return краткая информация о событии, которому пользователь поставил дизлайк
     */
    @Override
    public EventShortDto addDislike(int userId, int eventId) {
        User user = userRepository.findById(userId).get();
        Event event = eventService.getEventById(eventId);
        if (!event.getState().equals(EventState.PUBLISHED)) {
            log.error("Нельзя поставить dislike неопубликованному событию id{}.", eventId);
            throw new ForbiddenException(List.of(
                    new Error("eventId", "неверное значение").toString()),
                    String.format("Невозможно поставить dislike событию id%d.", eventId),
                    String.format("Cобытие id%d ещё не опубликовано.", eventId));
        }
        if (userId == event.getInitiator().getId()) {
            log.error("Пользователь id{} не может поставить dislike своему событию id{}.", userId, eventId);
            throw new ForbiddenException(List.of(
                    new Error("userId", "неверное значение").toString()),
                    String.format("Невозможно поставить dislike событию id%d.", eventId),
                    String.format("Пользователь id%d не может поставить dislike своему событию id%d.", userId,
                            eventId));
        }
        Optional<Like> dislike = likeRepository.findByUserAndEventAndIsLikeIsFalse(user, event);
        if (dislike.isPresent()) {
            log.error("Пользователь id{} уже поставил dislike событию id{}.", userId, eventId);
            throw new ConflictException(List.of(
                    new Error("eventId", "неверное значение").toString()),
                    String.format("Невозможно поставить dislike событию id%d.", eventId),
                    String.format("Пользователь id%d уже поставил dislike событию id%d.", userId, eventId));
        }
        Optional<Like> like = likeRepository.findByUserAndEventAndIsLikeIsTrue(user, event);
        if (like.isPresent()) {
            log.error("Пользователь id{} уже поставил like событию id{}.", userId, eventId);
            throw new ConflictException(List.of(
                    new Error("eventId", "неверное значение").toString()),
                    String.format("Невозможно поставить dislike событию id%d.", eventId),
                    String.format("Пользователь id%d уже поставил like событию id%d.", userId, eventId));
        }
        likeRepository.save(new Like(user, event, false));
        calculateEventLikesAndDislikes(event, event.getInitiator().getId());
        userService.calculateUserEventsLikesAndDislikes(event.getInitiator());
        log.info("Пользователь id{} поставил dislike событию id{}.", userId, eventId);
        return EventMapper.toEventDto(event);
    }

    /**
     * Метод позволяет пользователю получить краткую информацию по всем дизлайкам своего события
     *
     * @param userId  идентификатор пользователя
     * @param eventId идентификатор события
     * @return краткая информация по всем дизлайкам указанного события
     */
    @Override
    public List<LikeDto> getEventDislikesDto(Integer userId, int eventId) {
        return EventMapper.likesToDtoCollection(getEventDislikes(userId, eventId));
    }

    /**
     * Метод позволяет пользователю получить все дизлайки своего события по идентификатору
     *
     * @param userId  идентификатор пользователя
     * @param eventId идентификатор события
     * @return все дизлайки события
     */
    private List<Like> getEventDislikes(Integer userId, int eventId) {
        Event event = eventService.getEventById(eventId);
        if (!event.getState().equals(EventState.PUBLISHED)) {
            log.error("У неопубликованного события id{} не может быть дизлайков.", eventId);
            throw new ForbiddenException(List.of(
                    new Error("eventId", "неверное значение").toString()),
                    String.format("Невозможно получить дизлайки события id%d.", eventId),
                    String.format("У неопубликованного события id%d не может быть дизлайков.", eventId));
        }
        if (userId == null) {
            log.info("Администратор запросил все дизлайки события id{}.", eventId);
            return likeRepository.findAllByEventAndIsLikeIsFalse(event);
        }
        if (userId != event.getInitiator().getId()) {
            log.error("Пользователь id{} не является организатором события id{}.", userId, eventId);
            throw new ForbiddenException(List.of(
                    new Error("userId", "неверное значение").toString()),
                    String.format("Невозможно получить список дизлайков событию id%d.", eventId),
                    String.format("Пользователь id%d не является организатором события id%d.", userId, eventId));
        }
        log.info("Пользователь id{} запросил все дизлайки своего события id{}.", userId, eventId);
        return likeRepository.findAllByEventAndIsLikeIsFalse(event);
    }

    /**
     * Метод позволяет рассчитать рейтинг события на основе количества лайков и дизлайков
     *
     * @param event событие для расчёта рейтинга
     */
    private void calculateEventRating(Event event) {
        float rating;
        float likes = event.getLikes();
        float dislikes = event.getDislikes();
        if (likes > 0 && dislikes == 0) {
            rating = 5;
        } else if ((dislikes > 0 && likes == 0) || (likes == dislikes)) {
            rating = 0;
        } else {
            rating = likes / dislikes;
        }
        event.setRating(rating);
    }

    /**
     * Метод позволяет посчитать количество лайков и дизлайков события
     *
     * @param event  событие для расчёта
     * @param userId идентификатор пользователя (организатор события)
     */
    @Override
    public void calculateEventLikesAndDislikes(Event event, int userId) {
        List<Like> likes = getEventLikes(userId, event.getId());
        List<Like> dislikes = getEventDislikes(userId, event.getId());
        if (likes == null || likes.isEmpty()) {
            event.setLikes(0);
        } else {
            event.setLikes(likes.size());
        }
        if (dislikes == null || dislikes.isEmpty()) {
            event.setDislikes(0);
        } else {
            event.setDislikes(dislikes.size());
        }
        calculateEventRating(event);
    }
}
