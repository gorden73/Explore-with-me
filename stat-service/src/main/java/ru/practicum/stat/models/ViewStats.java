package ru.practicum.stat.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ViewStats {

    private int id;
    private String app;
    private String uri;
    private Integer hits;

    public ViewStats(String app, String uri, Integer hits) {
        this.app = app;
        this.uri = uri;
        this.hits = hits;
    }
}
