package ru.practicum.ewm.errors;

import lombok.Getter;
import lombok.Setter;

/**
 * Класс, описывающий возникшую программную ошибку
 *
 * @since 1.0
 */
@Getter
@Setter
public class Error {
    /**
     * Поле ошибки
     *
     * @since 1.0
     */
    private String field;
    /**
     * Описание ошибки
     *
     * @since 1.0
     */
    private String message;

    public Error(String field, String message) {
        this.field = field;
        this.message = message;
    }

    /**
     * Метод приводит объект текущего класса к читаемому виду
     *
     * @return объект текущего класса в читаемом виде
     * @since 1.0
     */
    @Override
    public String toString() {
        return String.format("Поле: %s. Ошибка: %s.", field, message);
    }
}
