package ru.practicum.ewm.apis.nonauthorizedusers.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.apis.admins.dtos.categories.CategoryDto;
import ru.practicum.ewm.services.CategoryService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;

@RestController
@RequestMapping(path = "/categories")
public class NonAuthorizedUserCategoryController {
    private final CategoryService categoryService;

    @Autowired
    public NonAuthorizedUserCategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public Collection<CategoryDto> getAllCategories(@RequestParam(defaultValue = "0")
                                                    @PositiveOrZero(message = "может быть равно или больше 0")
                                                    int from,
                                                    @RequestParam(defaultValue = "10")
                                                    @Positive(message = "может быть только больше 0")
                                                    int size) {
        return categoryService.getAllCategories(from, size);
    }

    @GetMapping("/{catId}")
    public CategoryDto getCategoryById(@PathVariable(name = "catId") int id) {
        return categoryService.getCategoryDtoById(id);
    }
}
