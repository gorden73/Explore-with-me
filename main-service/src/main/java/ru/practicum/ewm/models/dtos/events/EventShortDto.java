package ru.practicum.ewm.models.dtos.events;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.ewm.apis.admins.dtos.categories.CategoryDto;
import ru.practicum.ewm.apis.authorizedusers.dtos.users.UserShortDto;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
public class EventShortDto {
    private String annotation;
    private CategoryDto category;
    private int confirmedRequests;
    private String eventDate;
    private int id;
    private UserShortDto initiator;
    private boolean paid;
    private String title;
    private int views;
}
