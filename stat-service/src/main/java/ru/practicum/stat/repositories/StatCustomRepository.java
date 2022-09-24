package ru.practicum.stat.repositories;

import ru.practicum.stat.models.EndPointHit;

import java.util.List;

public interface StatCustomRepository {
    List<EndPointHit> findAllByUri(String start, String end, String uris, Boolean unique);
}
