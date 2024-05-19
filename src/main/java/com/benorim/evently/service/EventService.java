package com.benorim.evently.service;

import com.benorim.evently.entity.Event;
import com.benorim.evently.entity.EventlyUser;
import com.benorim.evently.exception.EventNotFoundException;
import com.benorim.evently.exception.EventUpdateOrCreateException;
import com.benorim.evently.exception.IllegalOperationException;
import com.benorim.evently.repository.EventRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class EventService {
    private static final Logger logger = LoggerFactory.getLogger(EventService.class);

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Event createEvent(Event event) {
        EventlyUser eventlyUser = AuthenticationService.getAuthenticatedUser();
        event.setCreatedAt(LocalDateTime.now());
        event.setCreatedBy(eventlyUser);
        try {
            return eventRepository.save(event);
        } catch (Exception e) {
            logger.error("Error creating event", e);
            throw new EventUpdateOrCreateException("Error creating event");
        }

    }

    public void deleteEvent(Long id) {
        validateEventOwner(id);

        eventRepository.deleteById(id);
    }

    public List<Event> getEvents(Integer limit, Integer offset) {
        return eventRepository.findAll(PageRequest.of(offset, limit));
    }

    public List<Event> getEvents(String city, String state) {
        if (StringUtils.isEmpty(city) && StringUtils.isEmpty(state)) {
            return getEvents();
        }

        if (StringUtils.isNotEmpty(city) && StringUtils.isEmpty(state)) {
            return eventRepository.findAllByAddressCity(city);
        }

        if (StringUtils.isEmpty(city) && StringUtils.isNotEmpty(state)) {
            return eventRepository.findAllByAddressState(state);
        }

        return eventRepository.findAllByAddressCityAndAddressState(city, state);
    }

    public List<Event> getEvents() {
        return eventRepository.findAll();
    }

    public Event getEventById(long id) {
        return eventRepository.findById(id).orElse(null);
    }

    public Event updateEvent(long id, Event event) {
        validateEventOwner(id);
        event.setId(id);
        try {
            return eventRepository.save(event);
        } catch (Exception e) {
            logger.error("Error updating event", e);
            throw new EventUpdateOrCreateException("Error updating event");
        }
    }

    private void validateEventOwner(Long id) {
        Event event = getEventById(id);
        if (event == null) {
            throw new EventNotFoundException("Event with id " + id + " not found");
        }

        if (!event.getCreatedBy().getId().equals(AuthenticationService.getAuthenticatedUser().getId())) {
            throw new IllegalOperationException("Not authorized to change event");
        }
    }
}
