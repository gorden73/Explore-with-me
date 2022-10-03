package ru.practicum.ewm.controllers.apis.admins.dtos.mappers;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.controllers.apis.admins.dtos.categories.CategoryDto;
import ru.practicum.ewm.models.Category;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Класс для работы с категорией событий и преобразования Dto в сущность и обратно
 *
 * @see Category
 * @since 1.0
 */
@Component
public class CategoryMapper {
    /**
     * Метод позволяет преобразовать сущность категории в Dto
     *
     * @param category сущность категории
     * @return Dto категории событий
     * @since 1.0
     */
    public static CategoryDto toDto(Category category) {
        return new CategoryDto(
                category.getId(),
                category.getName());
    }

    /**
     * Метод позволяет преобразовать Dto в сущность категории
     *
     * @param dto Dto категории событий
     * @return сущность категории
     * @since 1.0
     */
    public static Category toCategory(CategoryDto dto) {
        return new Category(
                dto.getName());
    }

    /**
     * Метод позволяет преобразовать коллекцию сущностей категорий в коллекцию Dto категорий
     *
     * @param categories коллекцию сущностей категорий
     * @return коллекцию Dto категорий
     * @since 1.0
     */
    public static Collection<CategoryDto> toDtoCollection(Collection<Category> categories) {
        return categories.stream()
                .map(CategoryMapper::toDto)
                .collect(Collectors.toList());
    }
}
