package ru.practicum.stat.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "hits")
public class ViewStats {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String app;
    private String uri;
    private Integer hits;
    @Column(name = "unique_hits")
    private Integer uniqueHits;


    public ViewStats(String app, String uri) {
        this.app = app;
        this.uri = uri;
    }
}
