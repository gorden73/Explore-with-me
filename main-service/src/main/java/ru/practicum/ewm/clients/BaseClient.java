package ru.practicum.ewm.clients;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import ru.practicum.ewm.models.dtos.stats.ViewStatsDto;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class BaseClient {
    protected final RestTemplate rest;

    public BaseClient(RestTemplate rest) {
        this.rest = rest;
    }

    protected ViewStatsDto[] get(String path, Map<String, Object> parameters) {
        ResponseEntity<ViewStatsDto[]> ewmServerResponse = rest.getForEntity(path, ViewStatsDto[].class, parameters);
        return ewmServerResponse.getBody();
    }

    protected <T> void post(String path, T body) {
        HttpEntity<T> requestEntity = new HttpEntity<>(body, defaultHeaders());
        rest.exchange(path, HttpMethod.POST, requestEntity, Object.class, Optional.empty());
    }

    private HttpHeaders defaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }
}
