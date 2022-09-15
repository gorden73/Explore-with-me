package ru.practicum.ewm.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "requests")
public class Request {
    private LocalDateTime created;
    @ManyToOne
    @JoinColumn(name = "event")
    private Event event;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @OneToOne
    @JoinColumn(name = "requester")
    private User requester;
    @Enumerated(EnumType.STRING)
    private RequestState state;

    public Request(LocalDateTime created, RequestState state) {
        this.created = created;
        this.state = state;
    }
}
