package ru.practicum.ewm.models.dto.stats;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class EndPointHitDto {
    private Integer id;
    private String app;
    private String uri;
    private String ip;
    private String timestamp;

    public EndPointHitDto(String app, String uri, String ip) {
        this.app = app;
        this.uri = uri;
        this.ip = ip;
    }
}
