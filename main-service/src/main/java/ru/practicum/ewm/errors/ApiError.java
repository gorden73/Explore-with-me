package ru.practicum.ewm.errors;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.practicum.ewm.exceptions.MainException;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Класс, подробно описыващий ошибку, возникшую в процессе работы приложения
 *
 * @since 1.0
 */
@Getter
@Setter
public class ApiError {
    /**
     * Список возникших программных ошибок
     *
     * @since 1.0
     */
    private List<String> errors;
    /**
     * Сообщение об ошибке
     *
     * @since 1.0
     */
    private String message;
    /**
     * Причина возникновения ошибки
     *
     * @since 1.0
     */
    private String reason;
    /**
     * HTTP код ошибки
     *
     * @since 1.0
     */
    private String status;
    /**
     * Дата и время возникновения ошибки в формате "yyyy-MM-dd HH:mm:ss"
     *
     * @since 1.0
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    /**
     * Конструктор позволяет сформировать объект ошибки, передаваемой клиенту, при возникновении MainException
     *
     * @param exception возникшее исключение
     * @param status    HTTP код ошибки
     * @since 1.0
     */
    public ApiError(MainException exception, ErrorStatus status) {
        this.errors = exception.getErrors();
        this.message = exception.getMessage();
        this.reason = exception.getReason();
        this.status = status.toString();
        this.timestamp = LocalDateTime.now();
    }

    /**
     * Конструктор позволяет сформировать объект ошибки, передаваемой клиенту, при возникновении
     * MethodArgumentNotValidException
     *
     * @param exception возникшее исключение
     * @param message   сообщение об ошибке
     * @param reason    причина возникновения ошибки
     * @param status    HTTP код ошибки
     * @since 1.0
     */
    public ApiError(MethodArgumentNotValidException exception, String message, String reason, ErrorStatus status) {
        this.errors = exception.getBindingResult().getFieldErrors().stream()
                .map(error -> new Error(
                        error.getField(),
                        error.getDefaultMessage()).toString())
                .collect(Collectors.toList());
        this.message = message;
        this.reason = reason;
        this.status = status.toString();
        this.timestamp = LocalDateTime.now();
    }

    /**
     * Конструктор позволяет сформировать объект ошибки, передаваемой клиенту, при возникновении
     * ConstraintViolationException
     *
     * @param exception возникшее исключение
     * @param message   сообщение об ошибке
     * @param reason    причина возникновения ошибки
     * @param status    HTTP код ошибки
     * @since 1.0
     */
    public ApiError(ConstraintViolationException exception, String message, String reason, ErrorStatus status) {
        this.errors = exception.getConstraintViolations().stream()
                .map(e -> new Error(StreamSupport.stream(e.getPropertyPath().spliterator(), false)
                        .reduce((first, second) -> second)
                        .orElse(null).toString(), e.getMessage()).toString())
                .collect(Collectors.toList());
        this.message = message;
        this.reason = reason;
        this.status = status.toString();
        this.timestamp = LocalDateTime.now();
    }

    /**
     * Конструктор позволяет сформировать объект ошибки, передаваемой клиенту, при возникновении Throwable
     *
     * @param exception возникшее исключение
     * @param reason    причина возникновения ошибки
     * @param status    HTTP код ошибки
     * @since 1.0
     */
    public ApiError(Throwable exception, String reason, ErrorStatus status) {
        this.errors = Arrays.stream(exception.getStackTrace())
                .map(err -> new Error(err.getMethodName(),
                        err.toString()).toString())
                .collect(Collectors.toList());
        this.message = "Произошла непредвиденная ошибка.";
        this.reason = reason;
        this.status = status.toString();
        this.timestamp = LocalDateTime.now();
    }

    /**
     * Конструктор позволяет сформировать объект ошибки, передаваемой клиенту, при возникновении DateTimeParseException
     *
     * @param exception возникшее исключение
     * @param message   сообщение об ошибке
     * @param reason    причина возникновения ошибки
     * @param status    HTTP код ошибки
     * @since 1.0
     */
    public ApiError(DateTimeParseException exception, String message, String reason, ErrorStatus status) {
        this.errors = Arrays.stream(exception.getStackTrace())
                .map(err -> new Error(err.getMethodName(),
                        err.toString()).toString())
                .collect(Collectors.toList());
        this.message = message;
        this.reason = reason;
        this.status = status.toString();
        this.timestamp = LocalDateTime.now();
    }
}
