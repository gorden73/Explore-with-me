package ru.practicum.ewm.controllers.apis.authorizedusers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.controllers.apis.authorizedusers.dtos.likes.DislikeDto;
import ru.practicum.ewm.controllers.apis.authorizedusers.dtos.likes.LikeDto;
import ru.practicum.ewm.models.dtos.events.EventShortDto;
import ru.practicum.ewm.services.LikeService;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

/**
 * Контроллер для работы авторизованного пользователя с лайками/дизлайками событий
 *
 * @since 1.1
 */
@RestController
@RequestMapping(path = "/users/{userId}/events/{eventId}")
@Validated
public class AuthorizedUserLikeController {
    /**
     * Сервис для работы с лайками/дизлайками событий
     *
     * @since 1.1
     */
    private final LikeService likeService;

    @Autowired
    public AuthorizedUserLikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    /**
     * Метод позволяет пользователю поставить лайк чужому событию
     *
     * @param userId  идентификатор пользователя
     * @param eventId идентификатор события
     * @return краткая информация о событии, которому пользователь поставил лайк
     * @since 1.1
     */
    @PostMapping("/like")
    public EventShortDto addLikeToEvent(@PathVariable int userId,
                                        @PathVariable int eventId) {
        return likeService.addLike(userId, eventId);
    }

    /**
     * Метод позволяет пользователю поставить дизлайк чужому событию
     *
     * @param userId  идентификатор пользователя
     * @param eventId идентификатор события
     * @return краткая информация о событии, которому пользователь поставил дизлайк
     * @since 1.1
     */
    @PostMapping("/dislike")
    public EventShortDto addDislikeToEvent(@PathVariable int userId,
                                           @PathVariable int eventId) {
        return likeService.addDislike(userId, eventId);
    }

    /**
     * Метод позволяет пользователю получить краткую информацию по всем лайкам своего события
     *
     * @param userId  идентификатор пользователя
     * @param eventId идентификатор события
     * @return краткая информация по всем лайкам указанного события
     * @since 1.1
     */
    @GetMapping("/like")
    public List<LikeDto> getEventLikes(@PathVariable @NotNull(message = "должен быть указан id пользователя")
                                       Integer userId,
                                       @PathVariable int eventId,
                                       @RequestParam(defaultValue = "0")
                                       @PositiveOrZero(message = "может быть равно или больше 0")
                                       int from,
                                       @RequestParam(defaultValue = "10")
                                       @Positive(message = "может быть только больше 0")
                                       int size) {
        return likeService.getEventLikesDto(userId, eventId, from, size);
    }

    /**
     * Метод позволяет пользователю получить краткую информацию по всем дизлайкам своего события
     *
     * @param userId  идентификатор пользователя
     * @param eventId идентификатор события
     * @return краткая информация по всем дизлайкам указанного события
     * @since 1.1
     */
    @GetMapping("/dislike")
    public List<DislikeDto> getEventDislikes(@PathVariable @NotNull(message = "должен быть указан id пользователя")
                                             Integer userId,
                                             @PathVariable int eventId,
                                             @RequestParam(defaultValue = "0")
                                             @PositiveOrZero(message = "может быть равно или больше 0")
                                             int from,
                                             @RequestParam(defaultValue = "10")
                                             @Positive(message = "может быть только больше 0")
                                             int size) {
        return likeService.getEventDislikesDto(userId, eventId, from, size);
    }
}
