package ru.practicum.ewm.models.dto.likes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.ewm.models.dto.events.EventShortDto;
import ru.practicum.ewm.models.dto.users.UserShortDto;

@Getter
@Setter
@AllArgsConstructor
public class LikeDto {
    private int id;
    private UserShortDto user;
    private EventShortDto event;
}
