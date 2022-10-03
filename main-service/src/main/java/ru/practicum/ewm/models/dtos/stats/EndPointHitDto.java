package ru.practicum.ewm.models.dtos.stats;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.ewm.controllers.apis.authorizedusers.dtos.mappers.EventMapper;

import java.time.LocalDateTime;

/**
 * Класс, описывающий Dto сущности записи данных о просмотрах эндпоинтов
 *
 * @since 1.0
 */
@Getter
@Setter
public class EndPointHitDto {
    /**
     * Идентификатор записи
     *
     * @since 1.0
     */
    private Integer id;
    /**
     * Идентификатор сервиса, для которого записывается информация
     *
     * @since 1.0
     */
    private String app;
    /**
     * URI, для которого был осуществлен запрос
     *
     * @since 1.0
     */
    private String uri;
    /**
     * IP-адрес пользователя, осуществившего запрос
     *
     * @since 1.0
     */
    private String ip;
    /**
     * Дата и время, когда был совершен запрос к эндпоинту (в формате "yyyy-MM-dd HH:mm:ss")
     *
     * @since 1.0
     */
    private String timestamp;

    public EndPointHitDto(String app, String uri, String ip) {
        this.app = app;
        this.uri = uri;
        this.ip = ip;
        this.timestamp = LocalDateTime.now().format(EventMapper.FORMATTER);
    }
}
