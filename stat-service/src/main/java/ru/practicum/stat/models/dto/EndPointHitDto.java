package ru.practicum.stat.models.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private String timestamp;

    public EndPointHitDto(String app, String uri, String ip, String timestamp) {
        this.app = app;
        this.uri = uri;
        this.ip = ip;
        this.timestamp = timestamp;
    }
}
