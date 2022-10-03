package ru.practicum.ewm.services;

import ru.practicum.ewm.controllers.apis.admins.dtos.categories.CategoryDto;

import java.util.Collection;

/**
 * Интерфейс сервиса для работы с категориями событий
 *
 * @see ru.practicum.ewm.models.Category
 * @see CategoryDto
 * @since 1.0
 */
public interface CategoryService {
    /**
     * Метод позволяет получить список Dto категорий событий
     *
     * @param from количество категорий, которые нужно пропустить для формирования списка (по умолчанию 0)
     * @param size количество категорий в списке (по умолчанию 10)
     * @return список Dto категорий событий
     * @since 1.0
     */
    Collection<CategoryDto> getAllCategories(int from, int size);

    /**
     * Метод позволяет получить Dto категории событий по идентификатору
     *
     * @param catId идентификатор события
     * @return Dto категории событий
     * @since 1.0
     */
    CategoryDto getCategoryDtoById(int catId);

    /**
     * Метод позволяет администратору создать новую категорию событий из Dto категории, переданного администратором
     *
     * @param categoryDto Dto категории, переданный администратором
     * @return Dto новой категории событий
     * @since 1.0
     */
    CategoryDto addCategory(CategoryDto categoryDto);

    /**
     * Метод позволяет администратору обновить имеющуюся категорию событий из Dto категории, переданного администратором
     *
     * @param categoryDto Dto категории, переданный администратором
     * @return Dto обновленной категории событий
     * @since 1.0
     */
    CategoryDto updateCategory(CategoryDto categoryDto);

    /**
     * Метод позволяет администратору удалить категорию событий по идентификатору
     *
     * @param id идентификатор категории
     * @since 1.0
     */
    void removeCategory(int id);
}
