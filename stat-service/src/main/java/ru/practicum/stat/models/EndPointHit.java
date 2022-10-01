package ru.practicum.stat.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Класс, описывающий сущность записи данных о просмотрах эндпоинтов
 *
 * @since 1.0
 */
@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "stats")
public class EndPointHit {
    /**
     * Идентификатор сущности
     *
     * @since 1.0
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

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
    private LocalDateTime timestamp;

    public EndPointHit(String app, String uri, String ip, LocalDateTime timestamp) {
        this.app = app;
        this.uri = uri;
        this.ip = ip;
        this.timestamp = timestamp;
    }
}
