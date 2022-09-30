package ru.practicum.ewm.models.dtos.events;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.ewm.apis.admins.dtos.categories.CategoryDto;
import ru.practicum.ewm.apis.authorizedusers.dtos.users.UserShortDto;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
public class EventFullDto {
    private String annotation;
    private CategoryDto category;
    private int confirmedRequests;
    private String createdOn;
    private String description;
    private String eventDate;
    private int id;
    private UserShortDto initiator;
    private boolean paid;
    private int participantLimit;
    private String publishedOn;
    private boolean requestModeration;
    private String state;
    private String title;
    private int views;
    private Integer likes;
    private Integer dislikes;
    private String rating;

    public EventFullDto(String annotation, CategoryDto category, int confirmedRequests, String createdOn,
                        String description, String eventDate, int id, UserShortDto initiator, boolean paid,
                        int participantLimit, boolean requestModeration, String state, String title, int views,
                        Integer likes, Integer dislikes, String rating) {
        this.annotation = annotation;
        this.category = category;
        this.confirmedRequests = confirmedRequests;
        this.createdOn = createdOn;
        this.description = description;
        this.eventDate = eventDate;
        this.id = id;
        this.initiator = initiator;
        this.paid = paid;
        this.participantLimit = participantLimit;
        this.requestModeration = requestModeration;
        this.state = state;
        this.title = title;
        this.views = views;
        this.likes = likes;
        this.dislikes = dislikes;
        this.rating = rating;
    }
}
