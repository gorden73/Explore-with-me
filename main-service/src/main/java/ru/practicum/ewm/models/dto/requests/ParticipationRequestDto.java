package ru.practicum.ewm.models.dto.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParticipationRequestDto {
    private String created;
    private Integer event;
    private int id;
    private Integer requester;
    private String state;

    public ParticipationRequestDto(Integer event, Integer requester) {
        this.event = event;
        this.requester = requester;
    }
}
