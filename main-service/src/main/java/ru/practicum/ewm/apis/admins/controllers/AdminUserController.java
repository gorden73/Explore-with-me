package ru.practicum.ewm.apis.admins.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.apis.admins.dtos.users.UserDto;
import ru.practicum.ewm.services.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;

@RestController
@RequestMapping(path = "/admin/users")
@Validated
public class AdminUserController {
    private final UserService userService;

    @Autowired
    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

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

    @PostMapping
    public UserDto addUser(@Valid @RequestBody UserDto userDto) {
        return userService.addUser(userDto);
    }

    @DeleteMapping("/{userId}")
    public void removeUser(@PathVariable int userId) {
        userService.removeUser(userId);
    }
}
