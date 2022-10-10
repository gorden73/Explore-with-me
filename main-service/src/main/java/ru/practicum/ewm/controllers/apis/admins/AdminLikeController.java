package ru.practicum.ewm.controllers.apis.admins;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.controllers.apis.admins.dtos.likes.AdminDislikeDto;
import ru.practicum.ewm.controllers.apis.admins.dtos.likes.AdminLikeDto;
import ru.practicum.ewm.services.LikeService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

/**
 * Контроллер для работы администратора с лайками/дизлайками событий
 *
 * @since 1.1
 */
@RestController
@RequestMapping(path = "/admin/events/{eventId}")
@Validated
public class AdminLikeController {
    private final LikeService likeService;

    @Autowired
    public AdminLikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    /**
     * Метод позволяет получить подробную информацию по всем лайкам указанного события
     *
     * @param eventId идентификатор события
     * @return подробная информация по всем лайкам указанного события
     * @since 1.1
     */
    @GetMapping("/like")
    public List<AdminLikeDto> getEventLikes(@PathVariable int eventId,
                                            @RequestParam(defaultValue = "0")
                                            @PositiveOrZero(message = "может быть равно или больше 0")
                                            int from,
                                            @RequestParam(defaultValue = "10")
                                            @Positive(message = "может быть только больше 0")
                                            int size) {
        return likeService.getEventAdminLikesDto(eventId, from, size);
    }

    /**
     * Метод позволяет получить подробную информацию по всем дизлайкам указанного события
     *
     * @param eventId идентификатор события
     * @return подробная информация по всем дизлайкам указанного события
     * @since 1.1
     */
    @GetMapping("/dislike")
    public List<AdminDislikeDto> getEventDislikes(@PathVariable int eventId,
                                                  @RequestParam(defaultValue = "0")
                                                  @PositiveOrZero(message = "может быть равно или больше 0")
                                                  int from,
                                                  @RequestParam(defaultValue = "10")
                                                  @Positive(message = "может быть только больше 0")
                                                  int size) {
        return likeService.getEventAdminDislikesDto(eventId, from, size);
    }
}
