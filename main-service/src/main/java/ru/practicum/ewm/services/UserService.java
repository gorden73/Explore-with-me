package ru.practicum.ewm.services;

import ru.practicum.ewm.controllers.apis.admins.dtos.users.UserDto;
import ru.practicum.ewm.controllers.apis.authorizedusers.dtos.users.NewUserRequest;
import ru.practicum.ewm.models.User;

import java.util.Collection;

/**
 * Интерфейс сервиса для работы с пользователями
 *
 * @see UserDto
 * @see NewUserRequest
 * @since 1.0
 */
public interface UserService {
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
    Collection<UserDto> getAllUsers(Integer[] ids, int from, int size);

    /**
     * Метод позволяет администратору создать нового пользователя
     *
     * @param userDto объект, описывающий основные свойства нового пользователя
     * @return объект, описывающий основные и дополнительные свойства созданного пользователя
     * @since 1.0
     */
    UserDto addUser(NewUserRequest userDto);

    /**
     * Метод позволяет администратору удалить пользователя по идентификатору
     *
     * @param userId идентификатор пользователя
     * @since 1.0
     */
    void removeUser(int userId);

    /**
     * Метод позволяет рассчитать рейтинг пользователя на основе рейтинга его событий
     *
     * @param user организатор событий
     * @since 1.1
     */
    void calculateUserRating(User user);
}
