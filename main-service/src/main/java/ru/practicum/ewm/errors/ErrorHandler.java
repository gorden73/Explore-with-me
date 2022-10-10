package ru.practicum.ewm.errors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.ewm.exceptions.BadRequestException;
import ru.practicum.ewm.exceptions.ConflictException;
import ru.practicum.ewm.exceptions.ForbiddenException;
import ru.practicum.ewm.exceptions.NotFoundException;

import javax.validation.ConstraintViolationException;
import java.time.format.DateTimeParseException;

/**
 * Класс для перехватывания исключений и их обработки
 *
 * @since 1.0
 */
@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    /**
     * Метод позволяет перехватить BadRequestException, сформировать и передать клиенту информацию о возникшей ошибке
     *
     * @param e перехваченное исключение
     * @return подробная информация о возникшей ошибке
     * @since 1.0
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleBadRequestException(final BadRequestException e) {
        log.error("Ошибка, {}", e.getMessage());
        return new ApiError(e, ErrorStatus.BAD_REQUEST);
    }

    /**
     * Метод позволяет перехватить MethodArgumentNotValidException, сформировать и передать клиенту информацию о
     * возникшей ошибке
     *
     * @param e перехваченное исключение
     * @return подробная информация о возникшей ошибке
     * @since 1.0
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleBadRequestException(final MethodArgumentNotValidException e) {
        log.error("Ошибка: {}", e.getMessage());
        return new ApiError(e, "Ошибка валидации входных данных.", "Неверно заполнены поля.",
                ErrorStatus.BAD_REQUEST);
    }

    /**
     * Метод позволяет перехватить ConstraintViolationException, сформировать и передать клиенту информацию о возникшей
     * ошибке
     *
     * @param e перехваченное исключение
     * @return подробная информация о возникшей ошибке
     * @since 1.0
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleBadRequestException(final ConstraintViolationException e) {
        log.error("Ошибка: {}", e.getMessage());
        return new ApiError(e, "Ошибка валидации входных данных.", "Неверно заполнены поля.",
                ErrorStatus.BAD_REQUEST);
    }

    /**
     * Метод позволяет перехватить DateTimeParseException, сформировать и передать клиенту информацию о возникшей ошибке
     *
     * @param e перехваченное исключение
     * @return подробная информация о возникшей ошибке
     * @since 1.0
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleBadRequestException(final DateTimeParseException e) {
        log.error("Ошибка: {}", e.getMessage());
        return new ApiError(e, "Ошибка валидации входных данных.", "Неверно заполнены поля.",
                ErrorStatus.BAD_REQUEST);
    }

    /**
     * Метод позволяет перехватить NotFoundException, сформировать и передать клиенту информацию о возникшей ошибке
     *
     * @param e перехваченное исключение
     * @return подробная информация о возникшей ошибке
     * @since 1.0
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFoundException(final NotFoundException e) {
        log.error("Ошибка: {}", e.getMessage());
        return new ApiError(e, ErrorStatus.NOT_FOUND);
    }

    /**
     * Метод позволяет перехватить ForbiddenException, сформировать и передать клиенту информацию о возникшей ошибке
     *
     * @param e перехваченное исключение
     * @return подробная информация о возникшей ошибке
     * @since 1.0
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiError handleForbiddenException(final ForbiddenException e) {
        log.error("Ошибка: {}", e.getMessage());
        return new ApiError(e, ErrorStatus.FORBIDDEN);
    }

    /**
     * Метод позволяет перехватить ConflictException, сформировать и передать клиенту информацию о возникшей ошибке
     *
     * @param e перехваченное исключение
     * @return подробная информация о возникшей ошибке
     * @since 1.0
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleConflictException(final ConflictException e) {
        log.error("Ошибка: {}", e.getMessage());
        return new ApiError(e, ErrorStatus.CONFLICT);
    }

    /**
     * Метод позволяет перехватить Throwable, сформировать и передать клиенту информацию о возникшей ошибке
     *
     * @param e перехваченное исключение
     * @return подробная информация о возникшей ошибке
     * @since 1.0
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleInternalServerErrorException(final Throwable e) {
        log.error("Ошибка: {}", e.getMessage());
        return new ApiError(e, e.getMessage(), ErrorStatus.INTERNAL_SERVER_ERROR);
    }
}
