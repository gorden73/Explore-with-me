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

    /**
     * Метод позволяет получить список Dto категорий событий
     *
     * @param from количество категорий, которые нужно пропустить для формирования списка (по умолчанию 0)
     * @param size количество категорий в списке (по умолчанию 10)
     * @return список Dto категорий событий
     * @since 1.0
     */
    @Override
    public Collection<CategoryDto> getAllCategories(int from, int size) {
        Pageable page = PageRequest.of(from, size);
        log.info("Запрошены все категории начиная с {} в размере {} на странице.", from, size);
        return CategoryMapper.toDtoCollection(categoryRepository.findAll(page).getContent());
    }

    /**
     * Метод позволяет получить Dto категории событий по идентификатору
     *
     * @param id идентификатор события
     * @return Dto категории событий
     * @since 1.0
     */
    @Override
    public CategoryDto getCategoryDtoById(int id) {
        log.info("Запрошена категория id{}", id);
        return CategoryMapper.toDto(getCategoryById(id));
    }

    /**
     * Метод позволяет получить категорию событий по идентификатору
     *
     * @param id идентификатор события
     * @return категория событий
     * @since 1.0
     */
    private Category getCategoryById(int id) {
        return categoryRepository.findById(id).orElseThrow(() -> new NotFoundException(List.of(
                new Error("id", "неверное значение " + id).toString()),
                "Невозможно получить категорию.",
                String.format("Категория с id%d не найдена.", id)));
    }

    /**
     * Метод позволяет администратору создать новую категорию событий из Dto категории, переданного администратором
     *
     * @param categoryDto Dto категории, переданный администратором
     * @return Dto новой категории событий
     * @since 1.0
     */
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

    /**
     * Метод позволяет администратору обновить имеющуюся категорию событий из Dto категории, переданного администратором
     *
     * @param categoryDto Dto категории, переданный администратором
     * @return Dto обновленной категории событий
     * @since 1.0
     */
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

    /**
     * Метод позволяет администратору удалить категорию событий по идентификатору
     *
     * @param id идентификатор категории
     * @since 1.0
     */
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
