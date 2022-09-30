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

/**
 * Класс для работы с эндпоинтами событий, наследующий {@link BaseClient}
 *
 * @since 1.0
 */
@Service
public class EventClient extends BaseClient {
    /**
     * Константа префикса пути POST запроса
     *
     * @since 1.0
     */
    private static final String API_PREFIX_HIT = "/hit";

    /**
     * Константа префикса пути GET запроса
     *
     * @since 1.0
     */
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

    /**
     * Метод позволяет добавить информацию о просмотренном эндпоинте
     *
     * @param app идентификатор сервиса, просмотревшего эндпоинт
     * @param uri путь запроса просмотренного эндпоинта
     * @param ip  ip-адрес пользователя, просмотревшего эндпоинт
     * @since 1.0
     */
    public void addHit(String app, String uri, String ip) {
        post(API_PREFIX_HIT, new EndPointHitDto(app, uri, ip));
    }

    /**
     * Метод позволяет получить список Dto статистики просмотров эндпоинтов, подходящих под заданные условия
     *
     * @param start  дата и время, не раньше которых должен быть выполнен просмотр эндпоинта
     * @param end    дата и время, не позже которых должен быть выполнен просмотр эндпоинта
     * @param uris   список путей запросов эндпоинтов
     * @param unique искать только уникальные запросы (только с уникальным ip-адресом)
     * @return список Dto статистики просмотров эндпоинтов
     * @since 1.0
     */
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

    /**
     * Метод позволяет закодировать передаваемый параметр с помощью URLEncoder
     *
     * @param param параметр для кодирования
     * @return закодированный параметр
     * @since 1.0
     */
    private String encodeParameter(String param) {
        try {
            return URLEncoder.encode(param, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
