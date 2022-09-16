package ru.practicum.ewm.services;

import ru.practicum.ewm.models.Complication;
import ru.practicum.ewm.models.dto.complications.ComplicationDto;
import ru.practicum.ewm.models.dto.complications.NewComplicationDto;

import java.util.Collection;

public interface ComplicationService {
    Collection<ComplicationDto> getAllComplications(Boolean pinned, int from, int size);

    ComplicationDto getComplicationDtoById(int id);

    Complication getComplicationById(int id);

    ComplicationDto addComplication(NewComplicationDto complicationDto);

    void removeComplicationById(int id);

    void removeEventFromComplication(int compId, int eventId);

    void addEventToComplication(int compId, int eventId);

    void unpinComplicationAtMainPage(int compId);

    void pinComplicationAtMainPage(int compId);
}
