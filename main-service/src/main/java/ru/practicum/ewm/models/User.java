package ru.practicum.ewm.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * Класс, описывающий пользователя как хранимую сущность
 *
 * @since 1.0
 */
@Entity
@Getter
@Setter
@Table(name = "users")
@NoArgsConstructor
public class User {
    /**
     * Идентификатор пользователя
     *
     * @since 1.0
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    /**
     * Адрес электронной почты пользователя
     *
     * @since 1.0
     */
    private String email;
    /**
     * Имя пользователя
     *
     * @since 1.0
     */
    private String name;

    public User(String email, String name) {
        this.email = email;
        this.name = name;
    }
}
