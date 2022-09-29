package ru.practicum.ewm.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

/**
 * Класс, описывающий подборку событий как хранимую сущность
 * @since 1.0
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "compilations")
public class Compilation {
    /**
     * Идентификатор подборки
     * @since 1.0
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    /**
     * Список событий {@link Event}, из которых состоит подборка
     * @since 1..0
     */
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "compilations_events",
            joinColumns = @JoinColumn(name = "compilation_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id"))
    @JsonManagedReference
    private Set<Event> events;
    /**
     * Закреплена ли подборка на главной странице
     * @since 1.0
     */
    private boolean pinned;
    /**
     * Заголовок подборки
     * @since 1.0
     */
    private String title;

    public Compilation(boolean pinned, String title) {
        this.pinned = pinned;
        this.title = title;
    }
}
