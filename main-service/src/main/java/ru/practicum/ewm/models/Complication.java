package ru.practicum.ewm.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Table(name = "complications")
public class Complication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToMany
    @JoinTable(
            name = "complications_events",
            joinColumns = @JoinColumn(name = "complication_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id"))
    @JsonIgnore
    private Set<Event> events;
    private boolean pinned;
    private String title;

    public Complication(boolean pinned, String title) {
        this.pinned = pinned;
        this.title = title;
    }
}
