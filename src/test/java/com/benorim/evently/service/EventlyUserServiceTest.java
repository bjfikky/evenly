package com.benorim.evently.service;

import com.benorim.evently.entity.EventlyUser;
import com.benorim.evently.repository.EventlyUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class EventlyUserServiceTest {
    @Mock
    private EventlyUserRepository eventlyUserRepository;

    @InjectMocks
    private EventlyUserService eventlyUserService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createUser() {
        EventlyUser user = new EventlyUser();
        when(eventlyUserRepository.save(any(EventlyUser.class))).thenReturn(user);

        EventlyUser result = eventlyUserService.createUser(user);

        assertNotNull(result);
        verify(eventlyUserRepository, times(1)).save(user);
    }

    @Test
    void findByEmail() {
        EventlyUser user = new EventlyUser();
        when(eventlyUserRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        EventlyUser result = eventlyUserService.findByEmail("test@example.com");

        assertNotNull(result);
        verify(eventlyUserRepository, times(1)).findByEmail("test@example.com");
    }

    @Test
    void findAll() {
        EventlyUser user1 = new EventlyUser();
        EventlyUser user2 = new EventlyUser();
        when(eventlyUserRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        List<EventlyUser> result = eventlyUserService.findAll();

        assertEquals(2, result.size());
        verify(eventlyUserRepository, times(1)).findAll();
    }

    @Test
    void updateUser_withValidId() {
        EventlyUser user = new EventlyUser();
        user.setId(1L);
        when(eventlyUserRepository.save(any(EventlyUser.class))).thenReturn(user);

        EventlyUser result = eventlyUserService.updateUser(user);

        assertNotNull(result);
        verify(eventlyUserRepository, times(1)).save(user);
    }

    @Test
    void updateUser_withNullId() {
        EventlyUser user = new EventlyUser();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            eventlyUserService.updateUser(user);
        });

        assertEquals("The id of the user cannot be null", exception.getMessage());
        verify(eventlyUserRepository, never()).save(any(EventlyUser.class));
    }

    @Test
    void userDetailsService_withValidUsername() {
        EventlyUser user = new EventlyUser();
        when(eventlyUserRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        UserDetailsService userDetailsService = eventlyUserService.userDetailsService();
        UserDetails result = userDetailsService.loadUserByUsername("test@example.com");

        assertNotNull(result);
        verify(eventlyUserRepository, times(1)).findByEmail("test@example.com");
    }

    @Test
    void userDetailsService_withInvalidUsername() {
        when(eventlyUserRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        UserDetailsService userDetailsService = eventlyUserService.userDetailsService();

        Exception exception = assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("invalid@example.com");
        });

        assertEquals("invalid@example.com", exception.getMessage());
        verify(eventlyUserRepository, times(1)).findByEmail("invalid@example.com");
    }
}