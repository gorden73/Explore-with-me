package ru.practicum.ewm.errors;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.practicum.ewm.exceptions.MainException;

import javax.validation.ConstraintViolationException;
import javax.validation.constraints.Pattern;
import java.net.BindException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Getter
@Setter
public class ApiError {
    private List<String> errors;
    private String message;
    private String reason;
    private String status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    public ApiError(MainException exception, ErrorStatus status) {
        this.errors = exception.getErrors();
        this.message = exception.getMessage();
        this.reason = exception.getReason();
        this.status = status.toString();
        this.timestamp = LocalDateTime.now();
    }

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

    public ApiError(ConstraintViolationException exception, String message, String reason,
                    ErrorStatus status) {
        this.errors = exception.getConstraintViolations().stream()
                .map(e -> new Error(StreamSupport.stream(e.getPropertyPath().spliterator(), false)
                        .reduce((first,second) -> second)
                        .orElse(null).toString(), e.getMessage()).toString())
                .collect(Collectors.toList());
        this.message = message;
        this.reason = reason;
        this.status = status.toString();
        this.timestamp = LocalDateTime.now();
    }

    public ApiError(Throwable exception, String reason, ErrorStatus status) {
        this.errors = Arrays.stream(exception.getStackTrace())
                .map(err -> new Error(err.getMethodName(),
                err.toString()).toString())
                .collect(Collectors.toList());
        this.message = "Произошла непредвиденная ошибка.";
        this.reason = reason;
        this.status = status.toString();
        this.timestamp = LocalDateTime.now();
        ;
    }
}
