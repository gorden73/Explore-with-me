package ru.practicum.ewm.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import ru.practicum.ewm.errors.Error;
import ru.practicum.ewm.exceptions.ConflictException;
import ru.practicum.ewm.exceptions.MainException;
import ru.practicum.ewm.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.models.dto.users.UserDto;
import ru.practicum.ewm.models.dto.mappers.UserMapper;
import ru.practicum.ewm.repositories.UserRepository;
import ru.practicum.ewm.services.UserService;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

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

    @Override
    public UserDto addUser(UserDto userDto) {
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

    @Override
    public void removeUser(int userId) {
        log.info("Удален пользователь id{}.", userId);
        userRepository.deleteById(userId);
    }
}
