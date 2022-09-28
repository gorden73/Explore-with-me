package ru.practicum.ewm.clients;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.ewm.models.dtos.stats.EndPointHitDto;
import ru.practicum.ewm.models.dtos.stats.ViewStatsDto;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static java.util.stream.Collectors.joining;

@Service
public class EventClient extends BaseClient {
    private static final String API_PREFIX_HIT = "/hit";
    private static final String API_PREFIX_STATS = "/stats";

    @Autowired
    public EventClient(@Value("${ewm-stats.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public void addHit(String app, String uri, String ip) {
        post(API_PREFIX_HIT, new EndPointHitDto(app, uri, ip));
    }

    public ViewStatsDto[] getStats(String start, String end, String[] uris, Boolean unique) {
        Map<String, String> times = Map.of(
                "start", start,
                "end", end
        );
        String encodedURL = times.keySet().stream()
                .map(key -> key + "=" + encodeParameter(times.get(key)))
                .collect(joining("&", API_PREFIX_STATS + "?", ""));
        Map<String, Object> parameters = Map.of(
                "uris", uris,
                "unique", unique
        );
        return get(encodedURL + "&uris={uris}&unique={unique}", parameters);
    }

    private String encodeParameter(String param) {
        try {
            return URLEncoder.encode(param, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
