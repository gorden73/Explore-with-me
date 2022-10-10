package ru.practicum.ewm.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Класс, описывающий категорию событий как хранимую сущность
 *
 * @since 1.0
 */
@Entity
@Getter
@Setter
@Table(name = "categories")
@NoArgsConstructor
public class Category {
    /**
     * Идентификатор категории
     *
     * @since 1.0
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    /**
     * Название категории
     *
     * @since 1.0
     */
    @NotNull
    @NotBlank
    @Column(name = "name")
    private String name;

    public Category(String name) {
        this.name = name;
    }
}
