package ru.practicum.ewm.controllers.apis.admins;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.controllers.apis.admins.dtos.likes.AdminDislikeDto;
import ru.practicum.ewm.controllers.apis.admins.dtos.likes.AdminLikeDto;
import ru.practicum.ewm.services.LikeService;

import java.util.List;

/**
 * Контроллер для работы администратора с лайками/дизлайками событий
 *
 * @since 1.1
 */
@RestController
@RequestMapping(path = "/admin/events")
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
    public List<AdminLikeDto> getEventLikes(@PathVariable int eventId) {
        return likeService.getEventAdminLikesDto(eventId);
    }

    /**
     * Метод позволяет получить подробную информацию по всем дизлайкам указанного события
     *
     * @param eventId идентификатор события
     * @return подробная информация по всем дизлайкам указанного события
     * @since 1.1
     */
    @GetMapping("/dislike")
    public List<AdminDislikeDto> getEventDislikes(@PathVariable int eventId) {
        return likeService.getEventAdminDislikesDto(eventId);
    }
}
