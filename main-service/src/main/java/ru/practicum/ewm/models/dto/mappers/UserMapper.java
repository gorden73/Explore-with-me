package ru.practicum.ewm.models.dto.mappers;

import ru.practicum.ewm.models.dto.users.UserDto;
import ru.practicum.ewm.models.dto.users.UserShortDto;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.models.User;

import java.util.Collection;
import java.util.stream.Collectors;

@Component
public class UserMapper {
    public static UserDto toDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail());
    }

    public static User toUser(UserDto dto) {
        return new User(
                dto.getEmail(),
                dto.getName());
    }

    public static UserShortDto toShortDto(User user) {
        return new UserShortDto(
                user.getId(),
                user.getName());
    }

    public static Collection<UserDto> toDtoCollection(Collection<User> userList) {
        return userList.stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }
}
