package com.benorim.evently.service;

import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.userdetails.UserDetails;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class JwtServiceImplTest {

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private JwtServiceImpl jwtService;

    private static final String TEST_USERNAME = "testUser";

    @BeforeEach
    void setUp() throws Exception {
        openMocks(this);
        setJwtBase64AuthSecret(jwtService, "73d062e2526ea22965c0d31374bc580e8402d7889596f7d9eebd5ce769606f67");

        when(userDetails.getUsername()).thenReturn(TEST_USERNAME);
    }

    private void setJwtBase64AuthSecret(JwtServiceImpl jwtService, String secret) throws Exception {
        Field field = JwtServiceImpl.class.getDeclaredField("baseAuth64Secret");
        field.setAccessible(true);
        field.set(jwtService, secret);
    }

    @Test
    void generateToken() {
        String token = jwtService.generateToken(userDetails);
        assertNotNull(token);
        String username = jwtService.extractUsername(token);
        assertEquals(TEST_USERNAME, username);
    }

    @Test
    void generateRefreshToken() {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", "USER");
        String refreshToken = jwtService.generateRefreshToken(extraClaims, userDetails);
        assertNotNull(refreshToken);
        String username = jwtService.extractUsername(refreshToken);
        assertEquals(TEST_USERNAME, username);
    }

    @Test
    void extractUsername() {
        String token = jwtService.generateToken(userDetails);
        String username = jwtService.extractUsername(token);
        assertEquals(TEST_USERNAME, username);
    }

    @Test
    void isTokenValid() {
        String token = jwtService.generateToken(userDetails);
        assertTrue(jwtService.isTokenValid(token, userDetails));
    }

    @Test
    void isTokenExpired() {
        String token = Jwts.builder()
                .subject(TEST_USERNAME)
                .issuedAt(new Date(System.currentTimeMillis() - 1000 * 60 * 60 * 24)) // 24 hours ago
                .expiration(new Date(System.currentTimeMillis() - 1000 * 60 * 60)) // 1 hour ago
                .signWith(jwtService.getSecretKey())
                .compact();
        assertTrue(jwtService.isTokenExpired(token));
    }
}
