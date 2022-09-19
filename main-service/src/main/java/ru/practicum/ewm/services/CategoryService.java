package ru.practicum.ewm.services;

import ru.practicum.ewm.models.dto.categories.CategoryDto;

import java.util.Collection;

public interface CategoryService {
    Collection<CategoryDto> getAllCategories(int from, int size);

    CategoryDto getCategoryDtoById(int catId);

    CategoryDto addCategory(CategoryDto categoryDto);

    CategoryDto updateCategory(CategoryDto categoryDto);

    void removeCategory(int id);
}
