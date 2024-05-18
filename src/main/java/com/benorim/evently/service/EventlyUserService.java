package com.benorim.evently.service;

import com.benorim.evently.entity.EventlyUser;
import com.benorim.evently.repository.EventlyUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class EventlyUserService {
    private static final Logger logger = LoggerFactory.getLogger(EventlyUserService.class);

    private final EventlyUserRepository eventlyUserRepository;

    public EventlyUserService(EventlyUserRepository eventlyUserRepository) {
        this.eventlyUserRepository = eventlyUserRepository;
    }

    public EventlyUser createUser(EventlyUser eventlyUser) {
        return eventlyUserRepository.save(eventlyUser);
    }

    public EventlyUser findByEmail(String email) {
        return eventlyUserRepository.findByEmail(email).orElse(null);
    }

    public List<EventlyUser> findAll() {
        return eventlyUserRepository.findAll();
    }

    public EventlyUser updateUser(EventlyUser eventlyUser) {
        if (eventlyUser.getId() == null) {
            logger.info("The id of the user cannot be null");
            throw new IllegalArgumentException("The id of the user cannot be null");
        }
        return eventlyUserRepository.save(eventlyUser);
    }

    public UserDetailsService userDetailsService() {
        return username -> eventlyUserRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }
}
