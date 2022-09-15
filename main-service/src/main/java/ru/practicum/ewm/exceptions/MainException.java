package ru.practicum.ewm.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class MainException extends RuntimeException {
    private List<String> errors;
    private String message;
    private String reason;
}
