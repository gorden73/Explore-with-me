package ru.practicum.ewm.apis.admins.dtos.mappers;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.apis.authorizedusers.dtos.users.NewUserRequest;
import ru.practicum.ewm.models.User;
import ru.practicum.ewm.apis.admins.dtos.users.UserDto;
import ru.practicum.ewm.apis.authorizedusers.dtos.users.UserShortDto;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Класс для работы с пользователями и преобразования Dto в сущность и обратно
 *
 * @see User
 * @since 1.0
 */
@Component
public class UserMapper {
    /**
     * Метод позволяет преобразовать сущность пользователя в Dto пользователя с подробной информацией
     * @param user сущность пользователя
     * @return Dto пользователя с подробной информацией
     * @since 1.0
     */
    public static UserDto toDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                String.format("%.1f", user.getRating()),
                user.getEmail());
    }

    /**
     * Метод позволяет преобразовать Dto нового пользователя в сущность пользователя
     * @param dto Dto нового пользователя
     * @return сущность пользователя
     * @since 1.0
     */
    public static User toUser(NewUserRequest dto) {
        return new User(
                dto.getEmail(),
                dto.getName());
    }

    /**
     * Метод позволяет преобразовать сущность пользователя в Dto пользователя с краткой информацией
     * @param user сущность пользователя
     * @return Dto пользователя с краткой информацией
     * @since 1.0
     */
    public static UserShortDto toShortDto(User user) {
        return new UserShortDto(
                user.getId(),
                user.getName(),
                String.format("%.1f", user.getRating()));

    }

    /**
     * Метод позволяет преобразовать коллекцию сущностей пользователей в коллекцию Dto пользователей с подробной
     * информацией
     * @param userList коллекция сущностей пользователей
     * @return коллекция Dto пользователей с подробной информацией
     * @since 1.0
     */
    public static Collection<UserDto> toDtoCollection(Collection<User> userList) {
        return userList.stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }
}
