package ru.practicum.ewm.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * Класс, описывающий сущность лайка/дизлайка
 *
 * @since 1.1
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
     *
     * @since 1.1
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    /**
     * Пользователь, поставивший лайк/дизлайк
     *
     * @since 1.1
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    /**
     * Событие, которому поставили лайк/дизлайк
     *
     * @since 1.1
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;
    /**
     * Объект является лайком(true) или дизлайком(false)
     *
     * @since 1.1
     */
    @Column(name = "is_like")
    private Boolean isLike;

    public Like(User user, Event event, Boolean isLike) {
        this.user = user;
        this.event = event;
        this.isLike = isLike;
    }
}
