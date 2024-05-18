package com.benorim.evently.mapper;

import com.benorim.evently.api.request.EventCreateRequest;
import com.benorim.evently.api.response.EventResponse;
import com.benorim.evently.entity.Address;
import com.benorim.evently.entity.Event;

import java.util.List;
import java.util.stream.Collectors;

public class EventMapper {
    public static Event mapCreateEventRequestToEvent(EventCreateRequest createEventRequest) {
        return new Event(
                createEventRequest.title(),
                createEventRequest.description(),
                new Address(
                        createEventRequest.addressLine1(),
                        createEventRequest.addressLine2(),
                        createEventRequest.city(),
                        createEventRequest.state(),
                        createEventRequest.zipCode()
                ),
                createEventRequest.imageUrl(),
                createEventRequest.startTime(),
                createEventRequest.endTime(),
                createEventRequest.additionalNotes()
        );
    }

    public static EventResponse mapEventToEventResponse(Event event) {
        return getEventResponse(event);
    }

    public static List<EventResponse> mapEventsToEventResponseList(List<Event> events) {
        return events.stream()
                .map(EventMapper::getEventResponse)
                .collect(Collectors.toList());
    }

    private static EventResponse getEventResponse(Event event) {
        if (event.getAddress() == null) {
            event.setAddress(new Address());
        }

        return new EventResponse(
                event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getAddress().getAddressLine1(),
                event.getAddress().getAddressLine2(),
                event.getAddress().getCity(),
                event.getAddress().getState(),
                event.getAddress().getZipCode(),
                event.getImageUrl(),
                event.getStartTime(),
                event.getEndTime(),
                event.getCreatedAt(),
                event.getUpdatedAt(),
                event.getAdditionalNotes());
    }
}
