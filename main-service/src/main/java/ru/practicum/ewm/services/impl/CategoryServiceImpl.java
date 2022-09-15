package ru.practicum.ewm.services.impl;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.ewm.errors.Error;
import ru.practicum.ewm.exceptions.ConflictException;
import ru.practicum.ewm.exceptions.NotFoundException;
import ru.practicum.ewm.models.Category;
import ru.practicum.ewm.models.dto.categories.CategoryDto;
import ru.practicum.ewm.models.dto.mappers.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.repositories.CategoryRepository;
import ru.practicum.ewm.services.CategoryService;

import java.util.Collection;
import java.util.List;

@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Collection<CategoryDto> getAllCategories(int from, int size) {
        Pageable page = PageRequest.of(from, size);
        log.info("Запрошены все категории начиная с {} в размере {} на странице.", from, size);
        return CategoryMapper.toDtoCollection(categoryRepository.findAll(page).getContent());
    }

    @Override
    public CategoryDto getCategoryById(int id) {
        log.info("Запрошена категория id{}", id);
        return CategoryMapper.toDto(categoryRepository.findById(id).orElseThrow());
    }

    @Override
    public CategoryDto addCategory(CategoryDto categoryDto) {
        Category category = CategoryMapper.toCategory(categoryDto);
        try {
            Category savedCategory = categoryRepository.save(category);
            log.info("Добавлена категория id{}.", savedCategory.getId());
            return CategoryMapper.toDto(savedCategory);
        } catch (DataIntegrityViolationException e) {
            log.error("Категория с названием '{}' уже существует.", category.getName());
            throw new ConflictException(List.of(
                    new Error("name", "должно быть уникальным").toString()),
                    "Невозможно создать категорию.",
                    String.format("Категория с названием '%s' уже существует.", category.getName()));
        }
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto) {
        Category category = categoryRepository.findById(categoryDto.getId())
                .orElseThrow(() -> new NotFoundException(List.of(
                        new Error("id", "неверное значение " + categoryDto.getId()).toString()),
                        "Невозможно обновить категорию.",
                        String.format("Категория с id%d не найдена.", categoryDto.getId())));
        category.setName(categoryDto.getName());
        try {
            Category savedCategory = categoryRepository.save(category);
            log.info("Обновлена категория id{}.", savedCategory.getId());
            return CategoryMapper.toDto(savedCategory);
        } catch (DataIntegrityViolationException e) {
            log.error("Категория с названием '{}' уже существует.", category.getName());
            throw new ConflictException(List.of(
                    new Error("name", "должно быть уникальным").toString()),
                    "Невозможно создать категорию.",
                    String.format("Категория с названием '%s' уже существует.", category.getName()));
        }
    }

    @Override
    public void removeCategory(int id) {
        log.info("Удалена категория id{}.", id);
        categoryRepository.deleteById(id);
    }
}
