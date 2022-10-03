package ru.practicum.ewm.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.controllers.apis.admins.dtos.categories.CategoryDto;
import ru.practicum.ewm.controllers.apis.admins.dtos.mappers.CategoryMapper;
import ru.practicum.ewm.errors.Error;
import ru.practicum.ewm.exceptions.ConflictException;
import ru.practicum.ewm.exceptions.ForbiddenException;
import ru.practicum.ewm.exceptions.NotFoundException;
import ru.practicum.ewm.models.Category;
import ru.practicum.ewm.repositories.CategoryRepository;
import ru.practicum.ewm.repositories.EventRepository;
import ru.practicum.ewm.services.CategoryService;

import java.util.Collection;
import java.util.List;

/**
 * Класс для работы с категориями событий, реализующий интерфейс {@link CategoryService}
 *
 * @since 1.0
 */
@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    /**
     * Интерфейс для работы с репозиторием категорий событий
     *
     * @see CategoryRepository
     * @since 1.0
     */
    private final CategoryRepository categoryRepository;
    /**
     * Интерфейс для работы с репозиторием событий
     *
     * @see EventRepository
     * @since 1.0
     */
    private final EventRepository eventRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, EventRepository eventRepository) {
        this.categoryRepository = categoryRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    public Collection<CategoryDto> getAllCategories(int from, int size) {
        Pageable page = PageRequest.of(from, size);
        log.info("Запрошены все категории начиная с {} в размере {} на странице.", from, size);
        return CategoryMapper.toDtoCollection(categoryRepository.findAll(page).getContent());
    }

    @Override
    public CategoryDto getCategoryDtoById(int id) {
        log.info("Запрошена категория id{}", id);
        return CategoryMapper.toDto(getCategoryById(id));
    }

    private Category getCategoryById(int id) {
        return categoryRepository.findById(id).orElseThrow(() -> new NotFoundException(List.of(
                new Error("id", "неверное значение " + id).toString()),
                "Невозможно получить категорию.",
                String.format("Категория с id%d не найдена.", id)));
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
        List<Integer> categoryEvents = eventRepository.findEventsByCategory(getCategoryById(id));
        if (!categoryEvents.isEmpty()) {
            throw new ForbiddenException(List.of(
                    new Error("id", "для удаления категории не должно быть событий, относящихся к этой " +
                            "категории id " + id).toString()),
                    "Невозможно удалить категорию.",
                    String.format("У категории id%d есть связанные события.", id));
        }
        log.info("Удалена категория id{}.", id);
        categoryRepository.deleteById(id);
    }
}
