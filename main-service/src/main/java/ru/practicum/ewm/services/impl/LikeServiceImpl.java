package ru.practicum.ewm.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.controllers.apis.admins.dtos.likes.AdminDislikeDto;
import ru.practicum.ewm.controllers.apis.admins.dtos.likes.AdminLikeDto;
import ru.practicum.ewm.controllers.apis.authorizedusers.dtos.likes.DislikeDto;
import ru.practicum.ewm.controllers.apis.authorizedusers.dtos.likes.LikeDto;
import ru.practicum.ewm.controllers.apis.authorizedusers.dtos.mappers.EventMapper;
import ru.practicum.ewm.errors.Error;
import ru.practicum.ewm.exceptions.ConflictException;
import ru.practicum.ewm.exceptions.ForbiddenException;
import ru.practicum.ewm.models.Event;
import ru.practicum.ewm.models.EventState;
import ru.practicum.ewm.models.Like;
import ru.practicum.ewm.models.User;
import ru.practicum.ewm.models.dtos.events.EventShortDto;
import ru.practicum.ewm.repositories.LikeRepository;
import ru.practicum.ewm.repositories.UserRepository;
import ru.practicum.ewm.services.EventService;
import ru.practicum.ewm.services.LikeService;
import ru.practicum.ewm.services.RequestService;
import ru.practicum.ewm.services.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Сервис для работы с лайками/дизлайками событий, реализующий интерфейс {@link LikeService}
 *
 * @since 1.1
 */
@Service
@Slf4j
public class LikeServiceImpl implements LikeService {
    private final EventService eventService;
    private final LikeRepository likeRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final RequestService requestService;

    @Autowired
    public LikeServiceImpl(EventService eventService, LikeRepository likeRepository, UserService userService,
                           UserRepository userRepository, RequestService requestService) {
        this.eventService = eventService;
        this.likeRepository = likeRepository;
        this.userService = userService;
        this.userRepository = userRepository;
        this.requestService = requestService;
    }

    @Override
    public EventShortDto addLike(int userId, int eventId) {
        Event event = eventService.getEventById(eventId);
        checkEventPublished(event);
        checkEventOwnerForUserIsTrue(userId, event.getInitiator().getId());
        checkEventDateTime(event.getEventDate());
        User user = userRepository.findById(userId).get();
        checkUserEventParticipation(event, user);
        Optional<Like> like = likeRepository.findByUserAndEventAndIsLikeIsTrue(user, event);
        if (like.isPresent()) {
            log.error("Пользователь id{} уже поставил like событию id{}.", userId, eventId);
            throw new ConflictException(List.of(
                    new Error("eventId", "неверное значение").toString()),
                    String.format("Невозможно поставить like событию id%d.", eventId),
                    String.format("Пользователь id%d уже поставил like событию id%d.", userId, eventId));
        }
        Optional<Like> dislike = likeRepository.findByUserAndEventAndIsLikeIsFalse(user, event);
        dislike.ifPresent(likeRepository::delete);
        likeRepository.save(new Like(user, event, true));
        calculateEventLikesAndDislikes(event, event.getInitiator().getId());
        userService.calculateUserRating(event.getInitiator());
        log.info("Пользователь id{} поставил like событию id{}.", userId, eventId);
        return EventMapper.toEventDto(event);
    }

    /**
     * Метод позволяет проверить участвовал ли пользователь в событии
     *
     * @since 1.1
     */
    private void checkUserEventParticipation(Event event, User user) {
        if (!requestService.checkUserEventParticipation(event, user)) {
            log.error("Нельзя поставить лайк/дизлайк событию, в котором не участковал пользователь.");
            throw new ForbiddenException(List.of(
                    new Error("eventId", "неверное значение").toString()),
                    "Невозможно выполнить операцию с лайками/дизлайками события.",
                    "Нельзя поставить лайк/дизлайк событию, в котором не участковал пользователь.");
        }
    }

    /**
     * Метод позволяет проверить возможность поставить лайк/дизлайк событию исходя из даты начала события
     *
     * @param eventDate дата начала события
     * @since 1.1
     */
    private void checkEventDateTime(LocalDateTime eventDate) {
        if (eventDate.isBefore(LocalDateTime.now())) {
            log.error("Нельзя поставить лайк/дизлайк до начала события.");
            throw new ForbiddenException(List.of(
                    new Error("eventId", "неверное значение").toString()),
                    "Невозможно выполнить операцию с лайками/дизлайками события.",
                    "Нельзя поставить лайк/дизлайк до начала события.");
        }
    }

    @Override
    public List<LikeDto> getEventLikesDto(Integer userId, int eventId, int from, int size) {
        Pageable page = PageRequest.of(from, size);
        return EventMapper.likesToDtoCollection(getEventLikes(userId, eventId, page));
    }

    @Override
    public List<AdminLikeDto> getEventAdminLikesDto(int eventId, int from, int size) {
        Pageable page = PageRequest.of(from, size);
        List<Like> eventLikes = getEventAdminLikes(eventId, page);
        eventLikes.forEach(like -> {
            calculateEventLikesAndDislikes(like.getEvent(), like.getEvent().getInitiator().getId());
            userService.calculateUserRating(like.getUser());
            userService.calculateUserRating(like.getEvent().getInitiator());
        });
        return EventMapper.likesToAdminDtoCollection(eventLikes);
    }

    @Override
    public List<AdminDislikeDto> getEventAdminDislikesDto(int eventId, int from, int size) {
        Pageable page = PageRequest.of(from, size);
        List<Like> eventDislikes = getEventAdminDislikes(eventId, page);
        eventDislikes.forEach(dislike -> {
            calculateEventLikesAndDislikes(dislike.getEvent(), dislike.getEvent().getInitiator().getId());
            userService.calculateUserRating(dislike.getUser());
            userService.calculateUserRating(dislike.getEvent().getInitiator());
        });
        return EventMapper.dislikesToAdminDtoCollection(eventDislikes);
    }

    /**
     * Метод позволяет администратору получить список лайков события по идентификатору
     *
     * @param eventId идентификатор события
     * @return список лайков своего события
     * @since 1.1
     */
    private List<Like> getEventAdminLikes(int eventId, Pageable page) {
        Event event = eventService.getEventById(eventId);
        checkEventPublished(event);
        log.info("Администратор запросил все лайки события id{}.", eventId);
        return likeRepository.findAllByEventAndIsLikeIsTrue(event, page);
    }

    /**
     * Метод позволяет пользователю получить список лайков своего события по идентификатору
     *
     * @param userId  идентификатор организатора
     * @param eventId идентификатор события
     * @return список лайков своего события
     * @since 1.1
     */
    private List<Like> getEventLikes(int userId, int eventId, Pageable page) {
        Event event = eventService.getEventById(eventId);
        checkEventPublished(event);
        checkEventOwnerForUserIsFalse(userId, event.getInitiator().getId());
        log.info("Пользователь id{} запросил все лайки своего события id{}.", userId, eventId);
        return likeRepository.findAllByEventAndIsLikeIsTrue(event, page);
    }

    /**
     * Метод позволяет проверить опубликовано ли событие
     *
     * @param event идентификатор события
     * @since 1.1
     */
    private void checkEventPublished(Event event) {
        int eventId = event.getId();
        if (!EventState.PUBLISHED.equals(event.getState())) {
            log.error("У неопубликованного события id{} не может быть лайков/дизлайков", eventId);
            throw new ForbiddenException(List.of(
                    new Error("eventId", "неверное значение").toString()),
                    String.format("Невозможно выполнить операцию с лайками/дизлайками события id%d.", eventId),
                    String.format("У неопубликованного события id%d не может быть лайков/дизлайков.", eventId));
        }
    }

    /**
     * Метод позволяет проверить является ли пользователь организатором события
     *
     * @param userId  идентификатор пользователя
     * @param ownerId идентификатор организатора события
     * @since 1.1
     */
    private void checkEventOwnerForUserIsTrue(int userId, int ownerId) {
        if (userId == ownerId) {
            log.error("Пользователь id{} не может поставить лайк/дизлайк своему событию.", userId);
            throw new ForbiddenException(List.of(
                    new Error("userId", "неверное значение").toString()),
                    "Невозможно поставить лайк/дизлайк событию.",
                    String.format("Пользователь id%d не может поставить лайк/дизлайк своему событию.", userId));
        }
    }

    /**
     * Метод позволяет проверить не является ли пользователь организатором события
     *
     * @param userId  идентификатор пользователя
     * @param ownerId идентификатор организатора события
     * @since 1.1
     */
    private void checkEventOwnerForUserIsFalse(int userId, int ownerId) {
        if (userId != ownerId) {
            log.error("Пользователь id{} не является организатором события.", userId);
            throw new ForbiddenException(List.of(
                    new Error("userId", "неверное значение").toString()),
                    "Невозможно получить список лайков событию id%d.",
                    String.format("Пользователь id%d не является организатором события.", userId));
        }
    }

    @Override
    public EventShortDto addDislike(int userId, int eventId) {
        Event event = eventService.getEventById(eventId);
        checkEventPublished(event);
        checkEventOwnerForUserIsFalse(userId, event.getInitiator().getId());
        checkEventDateTime(event.getEventDate());
        User user = userRepository.findById(userId).get();
        checkUserEventParticipation(event, user);
        Optional<Like> dislike = likeRepository.findByUserAndEventAndIsLikeIsFalse(user, event);
        if (dislike.isPresent()) {
            log.error("Пользователь id{} уже поставил dislike событию id{}.", userId, eventId);
            throw new ConflictException(List.of(
                    new Error("eventId", "неверное значение").toString()),
                    String.format("Невозможно поставить dislike событию id%d.", eventId),
                    String.format("Пользователь id%d уже поставил dislike событию id%d.", userId, eventId));
        }
        Optional<Like> like = likeRepository.findByUserAndEventAndIsLikeIsTrue(user, event);
        like.ifPresent(likeRepository::delete);
        likeRepository.save(new Like(user, event, false));
        calculateEventLikesAndDislikes(event, event.getInitiator().getId());
        userService.calculateUserRating(event.getInitiator());
        log.info("Пользователь id{} поставил dislike событию id{}.", userId, eventId);
        return EventMapper.toEventDto(event);
    }

    @Override
    public List<DislikeDto> getEventDislikesDto(Integer userId, int eventId, int from, int size) {
        Pageable page = PageRequest.of(from, size);
        return EventMapper.dislikesToDtoCollection(getEventDislikes(userId, eventId, page));
    }

    /**
     * Метод позволяет администратору получить список дизлайков события по индентификатору
     *
     * @param eventId идентификатор события
     * @return список дизлайков своего события
     * @since 1.1
     */
    private List<Like> getEventAdminDislikes(int eventId, Pageable page) {
        Event event = eventService.getEventById(eventId);
        checkEventPublished(event);
        log.info("Администратор запросил все дизлайки события id{}.", eventId);
        return likeRepository.findAllByEventAndIsLikeIsFalse(event, page);
    }

    /**
     * Метод позволяет пользователю получить список дизлайков своего события по индентификатору
     *
     * @param userId  идентификатор пользователя
     * @param eventId идентификатор события
     * @return список дизлайков своего события
     * @since 1.1
     */
    private List<Like> getEventDislikes(int userId, int eventId, Pageable page) {
        Event event = eventService.getEventById(eventId);
        checkEventPublished(event);
        checkEventOwnerForUserIsFalse(userId, event.getInitiator().getId());
        log.info("Пользователь id{} запросил все дизлайки своего события id{}.", userId, eventId);
        return likeRepository.findAllByEventAndIsLikeIsFalse(event, page);
    }

    /**
     * Метод позволяет рассчитать рейтинг события на основе количества лайков и дизлайков
     *
     * @param event событие для расчёта рейтинга
     * @since 1.1
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

    @Override
    public void calculateEventLikesAndDislikes(Event event, int userId) {
        Pageable page = PageRequest.of(0, Integer.MAX_VALUE);
        List<Like> likes = getEventLikes(userId, event.getId(), page);
        List<Like> dislikes = getEventDislikes(userId, event.getId(), page);
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
