package ru.practicum.ewm.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.controllers.apis.admins.dtos.mappers.UserMapper;
import ru.practicum.ewm.controllers.apis.admins.dtos.users.UserDto;
import ru.practicum.ewm.controllers.apis.authorizedusers.dtos.users.NewUserRequest;
import ru.practicum.ewm.errors.Error;
import ru.practicum.ewm.exceptions.ConflictException;
import ru.practicum.ewm.models.User;
import ru.practicum.ewm.repositories.UserRepository;
import ru.practicum.ewm.services.UserService;

import java.util.Collection;
import java.util.List;

/**
 * Класс для работы с пользователями, реализующий интерфейс {@link UserService}
 *
 * @see UserDto
 * @since 1.0
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    /**
     * Интерфейс для работы с репозиторием пользователя
     *
     * @see UserRepository
     * @since 1.0
     */
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
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
    @Override
    public Collection<UserDto> getAllUsers(Integer[] ids, int from, int size) {
        if (ids == null || ids.length == 0) {
            Pageable page = PageRequest.of(from, size);
            log.info("Запрошен список пользователей с {} в размере {}.", from, size);
            return UserMapper.toDtoCollection(userRepository.findAll(page).getContent());
        }
        log.info("Запрошен список пользователей {}.", (Object) ids);
        return UserMapper.toDtoCollection(userRepository.getAllUsers(ids));
    }

    /**
     * Метод позволяет администратору создать нового пользователя
     *
     * @param userDto объект, описывающий основные свойства нового пользователя
     * @return объект, описывающий основные и дополнительные свойства созданного пользователя
     * @since 1.0
     */
    @Override
    public UserDto addUser(NewUserRequest userDto) {
        User user = UserMapper.toUser(userDto);
        log.info("Добавлен новый пользователь email {}.", user.getEmail());
        try {
            return UserMapper.toDto(userRepository.save(user));
        } catch (DataIntegrityViolationException e) {
            log.error("Пользователь с email '{}' уже существует.", user.getEmail());
            throw new ConflictException(List.of(
                    new Error("email", "должно быть уникальным").toString()),
                    "Невозможно создать пользователя.",
                    String.format("Пользователь с email '%s' уже существует.", user.getEmail()));
        }
    }

    /**
     * Метод позволяет администратору удалить пользователя по идентификатору
     *
     * @param userId идентификатор пользователя
     * @since 1.0
     */
    @Override
    public void removeUser(int userId) {
        log.info("Удален пользователь id{}.", userId);
        userRepository.deleteById(userId);
    }

    @Override
    public User getUserById(int userId) {
        log.info("Запрошен пользователь id{}.", userId);
        return userRepository.findById(userId).get();
    }
}
