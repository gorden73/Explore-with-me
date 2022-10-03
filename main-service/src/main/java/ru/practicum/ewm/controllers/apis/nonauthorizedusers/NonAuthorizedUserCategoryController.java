package ru.practicum.ewm.controllers.apis.nonauthorizedusers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.controllers.apis.admins.dtos.categories.CategoryDto;
import ru.practicum.ewm.services.CategoryService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;

/**
 * Класс для работы неавторизованного пользователя с категориями событий
 *
 * @since 1.0
 */
@RestController
@RequestMapping(path = "/categories")
public class NonAuthorizedUserCategoryController {
    /**
     * Сервис для работы с категориями событий
     *
     * @since 1.0
     */
    private final CategoryService categoryService;

    @Autowired
    public NonAuthorizedUserCategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * Метод позволяет получить список Dto категории событий
     *
     * @param from количество категорий, которые нужно пропустить для формирования текущего набора (по умолчанию 0)
     * @param size количество категорий в наборе (по умолчанию 10)
     * @return список Dto категорий
     * @since 1.0
     */
    @GetMapping
    public Collection<CategoryDto> getAllCategories(@RequestParam(defaultValue = "0")
                                                    @PositiveOrZero(message = "может быть равно или больше 0")
                                                    int from,
                                                    @RequestParam(defaultValue = "10")
                                                    @Positive(message = "может быть только больше 0")
                                                    int size) {
        return categoryService.getAllCategories(from, size);
    }

    /**
     * Метод позволяет получить Dto категории по её идентификатору
     *
     * @param id идентификатор категории
     * @return Dto категории
     * @since 1.0
     */
    @GetMapping("/{catId}")
    public CategoryDto getCategoryById(@PathVariable(name = "catId") int id) {
        return categoryService.getCategoryDtoById(id);
    }
}
