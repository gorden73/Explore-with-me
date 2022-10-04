package ru.practicum.ewm.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * Класс, описывающий событие как хранимую сущность
 *
 * @since 1.0
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "events")
public class Event {
    /**
     * Идентификатор события
     *
     * @since 1.0
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    /**
     * Краткое описание события
     *
     * @since 1.0
     */
    @Column(name = "annotation")
    private String annotation;
    /**
     * Категория события
     *
     * @since 1.0
     */
    @ManyToOne
    @JoinColumn(name = "category")
    private Category category;
    /**
     * Полное описание события
     *
     * @since 1.0
     */
    @Column(name = "description")
    private String description;
    /**
     * Дата и время, на которые намечено событие (в формате "yyyy-MM-dd HH:mm:ss")
     *
     * @since 1.0
     */
    @Column(name = "event_date")
    private LocalDateTime eventDate;
    /**
     * Нужно ли оплачивать участие(по умолчанию false)
     *
     * @since 1.0
     */
    @Column(name = "paid")
    private boolean paid;
    /**
     * Ограничение на количество участников. Значение 0 - означает отсутствие ограничения. По умолчанию 0.
     *
     * @since 1.0
     */
    @Column(name = "participant_limit")
    private int participantLimit;
    /**
     * Нужна ли пре-модерация заявок на участие(по умолчанию true)
     *
     * @since 1.0
     */
    @Column(name = "request_moderation")
    private boolean requestModeration;
    /**
     * Заголовок события
     *
     * @since 1.0
     */
    @Column(name = "title")
    private String title;
    /**
     * Организатор события {@link User}
     *
     * @since 1.0
     */
    @ManyToOne
    @JoinColumn(name = "initiator")
    private User initiator;
    /**
     * Статус события {@link EventState}
     *
     * @since 1.0
     */
    @Enumerated(EnumType.STRING)
    private EventState state;
    /**
     * Дата и время публикации события
     *
     * @since 1.0
     */
    @Column(name = "published_on")
    private LocalDateTime publishedOn;
    /**
     * Дата и время создания события
     *
     * @since 1.0
     */
    @Column(name = "created_on")
    private LocalDateTime createdOn;
    /**
     * Количество одобренных заявок на участие в данном событии
     *
     * @since 1.0
     */
    @Transient
    private int confirmedRequests;
    /**
     * Количество просмотров события
     *
     * @since 1.0
     */
    @Transient
    private int views;
    /**
     * Подборки, в которых есть данное событие
     */
    @ManyToMany(mappedBy = "events", fetch = FetchType.LAZY)
    @JsonBackReference
    private Set<Compilation> compilations;
    /**
     * Доступно ли событие для участия(не исчерпан ли лимит количества участников)
     *
     * @since 1.0
     */
    @Column(name = "is_available")
    private Boolean isAvailable;
    /**
     * Количество лайков
     *
     * @since 1.0
     */
    @Column(name = "likes")
    private int likes;
    /**
     * Количество дизлайков
     *
     * @since 1.0
     */
    @Column(name = "dislikes")
    private int dislikes;
    /**
     * Рейтинг события (на основе лайков/дизлайков)
     *
     * @since 1.0
     */
    @Column(name = "rating")
    private float rating;

    public Event(String annotation, String description, LocalDateTime eventDate, boolean paid,
                 int participantLimit, boolean requestModeration, String title) {
        this.annotation = annotation;
        this.description = description;
        this.eventDate = eventDate;
        this.paid = paid;
        this.participantLimit = participantLimit;
        this.requestModeration = requestModeration;
        this.title = title;
    }
}
