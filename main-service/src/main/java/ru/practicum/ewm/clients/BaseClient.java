package ru.practicum.ewm.clients;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import ru.practicum.ewm.models.dtos.stats.ViewStatsDto;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Родительский класс для обмена данными по HTTP
 *
 * @since 1.0
 */
public class BaseClient {
    /**
     * Шаблон для передачи HTTP запроса
     *
     * @since 1.0
     */
    protected final RestTemplate rest;

    public BaseClient(RestTemplate rest) {
        this.rest = rest;
    }

    /**
     * Метод позволяет выполнить GET запрос по HTTP и получить в ответ список Dto статистики просмотров эндпоинтов
     *
     * @param path       путь запроса
     * @param parameters параметры запроса
     * @return список Dto статистики просмотров эндпоинтов
     * @since 1.0
     */
    protected ViewStatsDto[] get(String path, Map<String, Object> parameters) {
        ResponseEntity<ViewStatsDto[]> ewmServerResponse = rest.getForEntity(path, ViewStatsDto[].class, parameters);
        return ewmServerResponse.getBody();
    }

    /**
     * Метод позволяет выполнить POST запрос по HTTP
     *
     * @param path путь запроса
     * @param body тело запроса
     * @param <T>  тип возвращаемого значения
     * @since 1.0
     */
    protected <T> void post(String path, T body) {
        HttpEntity<T> requestEntity = new HttpEntity<>(body, defaultHeaders());
        rest.exchange(path, HttpMethod.POST, requestEntity, Object.class, Optional.empty());
    }

    /**
     * Метод позволяет добавить заголовки к запросу
     *
     * @return заголовки запроса
     * @since 1.0
     */
    private HttpHeaders defaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }
}
