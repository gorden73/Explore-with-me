package ru.practicum.ewm.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Класс, описывающий запрос на участие в событии как хранимую сущность
 *
 * @since 1.0
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "requests")
public class Request {
    /**
     * Дата и время создания запроса на участие в событии
     *
     * @since 1.0
     */
    private LocalDateTime created;
    /**
     * Событие, в котором предполагается участие
     *
     * @see Event
     * @since 1.0
     */
    @ManyToOne
    @JoinColumn(name = "event")
    private Event event;
    /**
     * Идентификатор запроса
     *
     * @since 1.0
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    /**
     * Пользователь, отправивший заявку на участие в событии
     *
     * @see User
     * @since 1.0
     */
    @OneToOne
    @JoinColumn(name = "requester")
    private User requester;
    /**
     * Статус заявки
     *
     * @see RequestState
     * @since 1.0
     */
    @Enumerated(EnumType.STRING)
    private RequestState state;

    public Request(LocalDateTime created, Event event, User requester, RequestState state) {
        this.created = created;
        this.event = event;
        this.requester = requester;
        this.state = state;
    }
}
