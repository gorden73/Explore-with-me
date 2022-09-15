package ru.practicum.stat.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "stats")
public class EndPointHit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String app;
    private String uri;
    private String ip;
    private long timestamp;

    public EndPointHit(String app, String uri, String ip, long timestamp) {
        this.app = app;
        this.uri = uri;
        this.ip = ip;
        this.timestamp = timestamp;
    }
}
