package com.benorim.evently.service;

import com.benorim.evently.entity.Event;
import com.benorim.evently.entity.EventlyUser;
import com.benorim.evently.exception.ResourceNotFoundException;
import com.benorim.evently.exception.EventUpdateOrCreateException;
import com.benorim.evently.exception.IllegalOperationException;
import com.benorim.evently.repository.EventRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventService eventService;

    @Mock
    private Authentication auth;

    @Mock
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(auth.getCredentials()).thenReturn("mockedPassword");
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @AfterEach
    public void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void createEvent_success() {
        Event event = new Event();
        EventlyUser user = new EventlyUser();
        user.setId(1L);


        event.setCreatedAt(LocalDateTime.now());
        event.setCreatedBy(user);

        when(eventRepository.save(any(Event.class))).thenReturn(event);
        when(AuthenticationService.getAuthenticatedUser()).thenReturn(user);

        Event result = eventService.createEvent(event);

        assertNotNull(result);
        assertEquals(user, result.getCreatedBy());
        verify(eventRepository, times(1)).save(event);
    }

    @Test
    void createEvent_failure() {
        Event event = new Event();
        EventlyUser user = new EventlyUser();
        user.setId(1L);

        when(AuthenticationService.getAuthenticatedUser()).thenReturn(user);
        when(eventRepository.save(any(Event.class))).thenThrow(RuntimeException.class);

        assertThrows(EventUpdateOrCreateException.class, () -> eventService.createEvent(event));
        verify(eventRepository, times(1)).save(event);
    }

    @Test
    void deleteEvent_success() {
        Event event = new Event();
        event.setId(1L);
        EventlyUser user = new EventlyUser();
        user.setId(1L);
        event.setCreatedBy(user);

        when(AuthenticationService.getAuthenticatedUser()).thenReturn(user);
        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(event));

        eventService.deleteEvent(1L);

        verify(eventRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteEvent_notOwner() {
        Event event = new Event();
        event.setId(1L);
        EventlyUser user = new EventlyUser();
        user.setId(1L);
        event.setCreatedBy(user);

        EventlyUser anotherUser = new EventlyUser();
        anotherUser.setId(2L);

        when(authenticationService.getAuthenticatedUser()).thenReturn(anotherUser);
        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(event));

        assertThrows(IllegalOperationException.class, () -> eventService.deleteEvent(1L));
        verify(eventRepository, never()).deleteById(1L);
    }

    @Test
    void getEvents_withLimitAndOffset() {
        Event event = new Event();
        when(eventRepository.findAll(any(PageRequest.class))).thenReturn(Collections.singletonList(event));

        List<Event> events = eventService.getEvents(1, 0);

        assertEquals(1, events.size());
        verify(eventRepository, times(1)).findAll(any(PageRequest.class));
    }

    @Test
    void getEvents_byCityAndState() {
        Event event = new Event();
        when(eventRepository.findAllByAddressCityAndAddressState(anyString(), anyString()))
                .thenReturn(Collections.singletonList(event));

        List<Event> events = eventService.getEvents("New York", "NY");

        assertEquals(1, events.size());
        verify(eventRepository, times(1)).findAllByAddressCityAndAddressState("New York", "NY");
    }

    @Test
    void getEventById_found() {
        Event event = new Event();
        event.setId(1L);
        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(event));

        Event result = eventService.getEventById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(eventRepository, times(1)).findById(1L);
    }

    @Test
    void getEventById_notFound() {
        when(eventRepository.findById(anyLong())).thenReturn(Optional.empty());

        Event result = eventService.getEventById(1L);

        assertNull(result);
        verify(eventRepository, times(1)).findById(1L);
    }

    @Test
    void updateEvent_success() {
        Event event = new Event();
        event.setId(1L);
        EventlyUser user = new EventlyUser();
        user.setId(1L);
        event.setCreatedBy(user);

        Event updatedEvent = new Event();
        updatedEvent.setId(1L);
        updatedEvent.setCreatedBy(user);

        when(AuthenticationService.getAuthenticatedUser()).thenReturn(user);
        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(event));
        when(eventRepository.save(any(Event.class))).thenReturn(updatedEvent);

        Event result = eventService.updateEvent(1L, updatedEvent);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(eventRepository, times(1)).save(updatedEvent);
    }

    @Test
    void updateEvent_notOwner() {
        Event event = new Event();
        event.setId(1L);
        EventlyUser user = new EventlyUser();
        user.setId(1L);
        event.setCreatedBy(user);

        EventlyUser anotherUser = new EventlyUser();
        anotherUser.setId(2L);

        when(AuthenticationService.getAuthenticatedUser()).thenReturn(anotherUser);
        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(event));

        Event updatedEvent = new Event();
        updatedEvent.setId(1L);

        assertThrows(IllegalOperationException.class, () -> eventService.updateEvent(1L, updatedEvent));
        verify(eventRepository, never()).save(updatedEvent);
    }

    @Test
    void validateEventOwner_notFound() {
        when(eventRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> eventService.deleteEvent(1L));
        verify(eventRepository, never()).deleteById(1L);
    }
}
