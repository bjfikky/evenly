package com.benorim.evently.api;

import com.benorim.evently.api.request.AuthRequest;
import com.benorim.evently.api.response.JwtResponse;
import com.benorim.evently.api.response.RegisterResponse;
import com.benorim.evently.entity.EventlyUser;
import com.benorim.evently.service.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
public class AuthApi {
    private final AuthenticationService authenticationService;

    public AuthApi(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody AuthRequest registerRequest) {
        EventlyUser user = authenticationService.register(registerRequest);
        RegisterResponse response = new RegisterResponse(user.getId(), user.getEmail(), user.getRole());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody AuthRequest loginRequest) {
        JwtResponse jwtResponse = authenticationService.login(loginRequest);
        return ResponseEntity.ok(jwtResponse);
    }
}
