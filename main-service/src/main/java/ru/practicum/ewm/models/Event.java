package ru.practicum.ewm.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
    private int id;
    /**
     * Краткое описание события
     *
     * @since 1.0
     */
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
    @Transient
    private int likes;
    @Transient
    private int dislikes;
    @Transient
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
