package com.benorim.evently.api;

import com.benorim.evently.api.request.EventCreateRequest;
import com.benorim.evently.api.response.ErrorResponse;
import com.benorim.evently.api.response.EventResponse;
import com.benorim.evently.api.response.EventsResponse;
import com.benorim.evently.entity.Event;
import com.benorim.evently.enums.ErrorState;
import com.benorim.evently.service.EventService;
import jakarta.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;

import static com.benorim.evently.mapper.EventMapper.mapCreateEventRequestToEvent;
import static com.benorim.evently.mapper.EventMapper.mapEventToEventResponse;
import static com.benorim.evently.mapper.EventMapper.mapEventsToEventResponseList;

@RestController
@RequestMapping("/api/v1/events")
public class EventApi {

    private final EventService eventService;

    public EventApi(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping
    public ResponseEntity<EventResponse> createEvent(@Valid @RequestBody EventCreateRequest createEventRequest) throws URISyntaxException {
        Event event = eventService.createEvent(mapCreateEventRequestToEvent(createEventRequest));
        return ResponseEntity.created(
                new URI( ServletUriComponentsBuilder.fromCurrentRequest().toUriString() + "/" + event.getId())).body(mapEventToEventResponse(event));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEvent(@PathVariable long id) {
        Event event = eventService.getEventById(id);
        if (event == null) {
            return ResponseEntity.notFound().build();
        }
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<EventsResponse> getEvents(
            @RequestParam(required = false) Integer limit,
            @RequestParam(required = false) Integer offset,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) String city
    ) {
        if (limit == null && offset != null) {
            limit = 100;
        }

        if (limit != null && offset == null) {
            offset = 0;
        }

        if (limit != null && limit > 0) {
            return ResponseEntity.ok(new EventsResponse(mapEventsToEventResponseList(eventService.getEvents(limit, offset))));
        }

        if (StringUtils.isNotEmpty(state) || StringUtils.isNotEmpty(city)) {
            return ResponseEntity.ok(new EventsResponse(mapEventsToEventResponseList(eventService.getEvents(city, state))));
        }

        return ResponseEntity.ok(new EventsResponse(mapEventsToEventResponseList(eventService.getEvents())));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventResponse> getEventById(@PathVariable long id) {
        Event event = eventService.getEventById(id);
        if (event == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(mapEventToEventResponse(event));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateEvent(@PathVariable long id, @RequestBody EventCreateRequest updateEventRequest) {
        if (StringUtils.isEmpty(updateEventRequest.title())) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Title cannot be empty", ErrorState.INVALID_TITLE));
        }

        if (eventService.getEventById(id) == null) {
            return ResponseEntity.notFound().build();
        }
        Event event = eventService.updateEvent(id, mapCreateEventRequestToEvent(updateEventRequest));
        return ResponseEntity.ok(mapEventToEventResponse(event));
    }
}
