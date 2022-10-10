package ru.practicum.ewm.controllers.apis.admins;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.controllers.apis.admins.dtos.categories.CategoryDto;
import ru.practicum.ewm.services.CategoryService;

import javax.validation.Valid;

/**
 * Контроллер для работы администратора с категориями
 *
 * @since 1.0
 */
@RestController
@RequestMapping(path = "/admin/categories")
@Validated
public class AdminCategoryController {
    /**
     * Сервис для работы с категориями
     *
     * @see CategoryService
     * @since 1.0
     */
    private final CategoryService categoryService;

    @Autowired
    public AdminCategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * Метод позволяет создать новую категорию
     *
     * @param categoryDto объект, описывающий основные свойства категории, которые задает администратор
     * @return созданный объект, описывающий основные и дополнительные свойства категории
     * @since 1.0
     */
    @PostMapping
    public CategoryDto addCategory(@Valid @RequestBody CategoryDto categoryDto) {
        return categoryService.addCategory(categoryDto);
    }

    /**
     * Метод позволяет обновить имеющуюся категорию
     *
     * @param categoryDto объект, описывающий свойства для обновления категории, которые задает администратор
     * @return обновленный объект категории
     * @since 1.0
     */
    @PatchMapping
    public CategoryDto updateCategory(@Valid @RequestBody CategoryDto categoryDto) {
        return categoryService.updateCategory(categoryDto);
    }

    /**
     * Метод позволяет удалить имеющуюся категорию по идентификатору
     *
     * @param catId идентификатор категории
     * @since 1.0
     */
    @DeleteMapping("/{catId}")
    public void removeCategory(@PathVariable int catId) {
        categoryService.removeCategory(catId);
    }
}
