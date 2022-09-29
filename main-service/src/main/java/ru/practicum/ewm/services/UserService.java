package ru.practicum.ewm.services;

import ru.practicum.ewm.apis.authorizedusers.users.NewUserRequest;
import ru.practicum.ewm.apis.authorizedusers.users.UserDto;

import java.util.Collection;

public interface UserService {
    Collection<UserDto> getAllUsers(Integer[] ids, int from, int size);

    UserDto addUser(NewUserRequest userDto);

    void removeUser(int userId);
}
