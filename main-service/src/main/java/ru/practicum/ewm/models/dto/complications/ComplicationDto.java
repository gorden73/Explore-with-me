package ru.practicum.ewm.models.dto.complications;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class ComplicationDto extends NewComplicationDto {
    private int id;

    public ComplicationDto(Set<Integer> events, int id, boolean pinned, String title) {
        super(events, pinned, title);
        this.id = id;
    }
}
