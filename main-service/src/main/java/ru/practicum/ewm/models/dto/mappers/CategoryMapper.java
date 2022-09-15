package ru.practicum.ewm.models.dto.mappers;

import ru.practicum.ewm.models.Category;
import ru.practicum.ewm.models.dto.categories.CategoryDto;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.stream.Collectors;

@Component
public class CategoryMapper {
    public static CategoryDto toDto(Category category) {
        return new CategoryDto(
                category.getId(),
                category.getName());
    }

    public static Category toCategory(CategoryDto dto) {
        return new Category(
                dto.getName());
    }

    public static Collection<CategoryDto> toDtoCollection(Collection<Category> categories) {
        return categories.stream()
                .map(CategoryMapper::toDto)
                .collect(Collectors.toList());
    }
}
