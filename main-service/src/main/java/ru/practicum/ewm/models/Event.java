package ru.practicum.ewm.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String annotation;
    @ManyToOne
    @JoinColumn(name = "category")
    private Category category;
    private String description;
    @Column(name = "event_date")
    private LocalDateTime eventDate;
    private boolean paid;
    @Column(name = "participant_limit")
    private int participantLimit;
    @Column(name = "request_moderation")
    private boolean requestModeration;
    private String title;
    @ManyToOne
    @JoinColumn(name = "initiator")
    private User initiator;
    @Enumerated(EnumType.STRING)
    private EventState state;
    @Column(name = "published_on")
    private LocalDateTime publishedOn;
    @Column(name = "created_on")
    private LocalDateTime createdOn;
    @Transient
    private int confirmedRequests;
    @Transient
    private int views;
    @ManyToMany(mappedBy = "events")
    @JsonIgnore
    private Set<Complication> complications;
    @Column(name = "is_available")
    private Boolean isAvailable;

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
