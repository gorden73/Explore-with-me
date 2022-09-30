package ru.practicum.ewm.apis.admins.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.apis.authorizedusers.dtos.users.NewUserRequest;
import ru.practicum.ewm.apis.authorizedusers.dtos.users.UserDto;
import ru.practicum.ewm.services.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;

/**
 * Контроллер для работы администратора с пользователями
 *
 * @since 1.0
 */
@RestController
@RequestMapping(path = "/admin/users")
@Validated
public class AdminUserController {
    /**
     * Сервис для работы с пользователями
     *
     * @see UserService
     * @since 1.0
     */
    private final UserService userService;

    @Autowired
    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Метод позволяет получить информацию обо всех пользователях (учитываются параметры ограничения выборки), либо о
     * конкретных (учитываются указанные идентификаторы)
     *
     * @param ids  список идентификаторов пользователей
     * @param from количество элементов, которые нужно пропустить для формирования текущего набора(по умолчанию 0)
     * @param size количество элементов в наборе(по умолчанию 10)
     * @return полная информация обо всех пользователях, подходящих под заданные условия
     * @since 1.0
     */
    @GetMapping
    public Collection<UserDto> getAllUsers(@RequestParam(required = false) Integer[] ids,
                                           @RequestParam(defaultValue = "0")
                                           @PositiveOrZero(message = "может быть равно или больше 0")
                                           int from,
                                           @RequestParam(defaultValue = "10")
                                           @Positive(message = "может быть только больше 0")
                                           int size) {
        return userService.getAllUsers(ids, from, size);
    }

    /**
     * Метод позволяет создать нового пользователя
     *
     * @param userDto объект, описывающий основные свойства нового пользователя
     * @return объект, описывающий основные и дополнительные свойства созданного пользователя
     * @since 1.0
     */
    @PostMapping
    public UserDto addUser(@Valid @RequestBody NewUserRequest userDto) {
        return userService.addUser(userDto);
    }

    /**
     * Метод позволяет удалить пользователя по идентификатору
     *
     * @param userId идентификатор пользователя
     * @since 1.0
     */
    @DeleteMapping("/{userId}")
    public void removeUser(@PathVariable int userId) {
        userService.removeUser(userId);
    }
}
