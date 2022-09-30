package ru.practicum.ewm.services;

import ru.practicum.ewm.models.User;
import ru.practicum.ewm.models.dto.users.UserDto;

import java.util.Collection;

public interface UserService {
    Collection<UserDto> getAllUsers(Integer[] ids, int from, int size);

    UserDto addUser(UserDto userDto);

    void removeUser(int userId);

    /**
     * Метод позволяет рассчитать количество лайков и дизлайков событиям пользователя
     *
     * @param user организатор событий
     */
    void calculateUserEventsLikesAndDislikes(User user);
}
