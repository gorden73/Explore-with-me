package ru.practicum.stat.repositories;

import ru.practicum.stat.models.EndPointHit;

import java.util.List;

/**
 * Кастомный интерфейс для работы с репозиторием статистики
 *
 * @since 1.0
 */
public interface StatCustomRepository {
    /**
     * Метод позволяет получить список записей данных о просмотрах эндпоинтов
     *
     * @param start  дата и время, не раньше которых должна быть добавлена информация о просмотрах эндпоинтов
     * @param end    дата и время, не раньше которых должна быть добавлена информация о просмотрах эндпоинтов
     * @param uri    URI, для которых надо предоставить статистику просмотров
     * @param unique выполнять поиск только для уникальных просмотров (по уникальным Ip-адресам)
     * @return список записей данных о просмотрах эндпоинтов
     * @since 1.0
     */
    List<EndPointHit> findAllByUri(String start, String end, String uri, Boolean unique);
}
