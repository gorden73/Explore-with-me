package ru.practicum.ewm.controllers.apis.authorizedusers.dtos.requests;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

/**
 * Класс Dto сущности запроса на участие в событии, описывающий основные свойства запроса, для взаимодействия с клиентом
 *
 * @since 1.0
 */
@Getter
@Setter
public class ParticipationRequestDto {
    /**
     * Дата и время создания запроса на участие в событии в формате "yyyy-MM-dd HH:mm:ss"
     *
     * @since 1.0
     */
    private String created;
    /**
     * Идентификатор события, на участие в котором создан запрос
     *
     * @since 1.0
     */
    private Integer event;
    /**
     * Идентификатор запроса
     *
     * @since 1.0
     */
    private int id;
    /**
     * Идентификатор пользователя, создавшего запрос на участие в событии
     *
     * @since 1.0
     */
    private Integer requester;
    /**
     * Статус запроса на участие в событии
     *
     * @since 1.0
     */
    private String status;

    public ParticipationRequestDto(Integer event, Integer requester) {
        this.event = event;
        this.requester = requester;
    }
}
