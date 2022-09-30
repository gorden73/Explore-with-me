package ru.practicum.ewm.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * Класс, описывающий сущность лайка/дизлайка
 */
@Getter
@Setter
@Entity
@Table(name = "likes")
@NoArgsConstructor
@AllArgsConstructor
public class Like {
    /**
     * Идентификатор лайка/дизлайка
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    /**
     * Пользователь, поставивший лайк/дизлайк
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    /**
     * Событие, которому поставили лайк/дизлайк
     */
    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;
    /**
     * Объект является лайком(true) или дизлайком(false)
     */
    @Column(name = "is_like")
    private Boolean isLike;

    public Like(User user, Event event, Boolean isLike) {
        this.user = user;
        this.event = event;
        this.isLike = isLike;
    }
}
