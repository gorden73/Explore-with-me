package ru.practicum.stat.models.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    public EndPointHitDto(String app, String uri, String ip) {
        this.app = app;
        this.uri = uri;
        this.ip = ip;
    }
}
