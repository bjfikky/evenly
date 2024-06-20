package com.benorim.evently.service;

import com.benorim.evently.api.request.AuthRequest;
import com.benorim.evently.api.response.JwtResponse;
import com.benorim.evently.entity.EventlyUser;
import com.benorim.evently.enums.Role;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class AuthenticationService {
    private final EventlyUserService eventlyUserService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;


    public AuthenticationService(EventlyUserService eventlyUserService, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.eventlyUserService = eventlyUserService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public EventlyUser register(AuthRequest registerRequest) {
        EventlyUser eventlyUser = new EventlyUser();
        eventlyUser.setEmail(registerRequest.email());
        eventlyUser.setPassword(passwordEncoder.encode(registerRequest.password()));
        eventlyUser.setOrganizationEmail(registerRequest.organizationEmail());
        eventlyUser.setOrganizationName(registerRequest.organizationName());
        eventlyUser.setRole(Role.USER);
        eventlyUser.setEnabled(true);
        eventlyUser.setAccountNonLocked(true);

        return eventlyUserService.createUser(eventlyUser);
    }

    public JwtResponse login(AuthRequest loginRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password()));
        EventlyUser eventlyUser = eventlyUserService.findByEmail(loginRequest.email());
        String token = jwtService.generateToken(eventlyUser);
        String refreshToken = jwtService.generateRefreshToken(new HashMap<>(), eventlyUser);
        return new JwtResponse(eventlyUser.getEmail(), token, refreshToken);
    }

    public static EventlyUser getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (EventlyUser) authentication.getPrincipal();
    }
}
