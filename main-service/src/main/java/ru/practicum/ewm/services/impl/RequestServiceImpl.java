package ru.practicum.ewm.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.models.Event;
import ru.practicum.ewm.models.Request;
import ru.practicum.ewm.models.RequestState;
import ru.practicum.ewm.models.User;
import ru.practicum.ewm.models.dto.mappers.RequestMapper;
import ru.practicum.ewm.models.dto.requests.ParticipationRequestDto;
import ru.practicum.ewm.repositories.EventRepository;
import ru.practicum.ewm.repositories.RequestRepository;
import ru.practicum.ewm.repositories.UserRepository;
import ru.practicum.ewm.services.RequestService;

import javax.servlet.http.Part;
import java.util.Collection;
import java.util.List;

@Service
@Slf4j
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Autowired
    public RequestServiceImpl(RequestRepository requestRepository, UserRepository userRepository,
                              EventRepository eventRepository) {
        this.requestRepository = requestRepository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    public Collection<ParticipationRequestDto> getEventRequests(int userId, int eventId) {
        User user = userRepository.findById(userId).orElseThrow(); // exception needed
        Event event = eventRepository.findEventByIdAndInitiator(eventId, user).orElseThrow(); //exception needed
        return RequestMapper.toDtoCollection(requestRepository.getRequestsByEventAndRequester(event, user));
    }

    @Override
    public ParticipationRequestDto confirmEventRequest(int userId, int eventId, int reqId) throws IllegalAccessException {
        User user = userRepository.findById(userId).orElseThrow(); // exception needed
        Event event = eventRepository.findEventByIdAndInitiator(eventId, user).orElseThrow();// exception needed
        Request request = requestRepository.findById(reqId).orElseThrow(); // exception needed
        if (request.getState().equals(RequestState.CONFIRM)) {
            throw new IllegalArgumentException();//exception needed
        }
        if (event.getParticipantLimit() == requestRepository.getConfirmedRequests(eventId)) {
            throw new IllegalAccessException(); //exception needed
        }
        if (request.getEvent().equals(event)) {
            request.setState(RequestState.CONFIRM);
        }
        if (event.getParticipantLimit() + 1 == requestRepository.getConfirmedRequests(eventId)) {
            List<Request> pendingRequests = requestRepository.findRequestsByEventAndState(event, RequestState.PENDING);
            for (Request req : pendingRequests) {
                req.setState(RequestState.REJECT);
            }
            requestRepository.saveAll(pendingRequests);
            event.setIsAvailable(false);
            eventRepository.save(event);
        }
        return RequestMapper.toDto(requestRepository.save(request));
    }

    @Override
    public ParticipationRequestDto rejectEventRequest(int userId, int eventId, int reqId) throws IllegalAccessException {
        User user = userRepository.findById(userId).orElseThrow(); // exception needed
        Event event = eventRepository.findEventByIdAndInitiator(eventId, user).orElseThrow();// exception needed
        Request request = requestRepository.findById(reqId).orElseThrow(); // exception needed
        if (request.getState().equals(RequestState.CONFIRM)) {
            throw new IllegalArgumentException();//exception needed
        }
        if (request.getEvent().equals(event)) {
            request.setState(RequestState.REJECT);
        }
        return RequestMapper.toDto(requestRepository.save(request));
    }

    @Override
    public Collection<ParticipationRequestDto> getUserRequests(int userId) {
        User user = userRepository.findById(userId).get();
        return RequestMapper.toDtoCollection(requestRepository.findRequestsByRequester(user));
    }

    @Override
    public ParticipationRequestDto addRequest(int userId, ParticipationRequestDto requestDto) {
        User user = userRepository.findById(userId).get();
        Event event = eventRepository.findById(requestDto.getEvent()).orElseThrow();// exception needed
        Request request = RequestMapper.toRequest(requestDto);
        request.setRequester(user);
        request.setEvent(event);
        return RequestMapper.toDto(requestRepository.save(request));
    }

    @Override
    public ParticipationRequestDto cancelRequestByUser(int userId, int requestId) {
        User user = userRepository.findById(userId).get();
        Request request = requestRepository.findRequestByIdAndRequester(requestId, user);
        request.setState(RequestState.CANCEL);
        return RequestMapper.toDto(requestRepository.save(request));
    }
}
