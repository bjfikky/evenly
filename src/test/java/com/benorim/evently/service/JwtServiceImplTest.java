package com.benorim.evently.service;

import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.security.core.userdetails.UserDetails;

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

    private JwtServiceImpl jwtService;

    private static final String BASE_64_STRING = "88eYaDwOqL5v3vjfNlAdJnc90YdV5ANgsx483qa5PWM=";
    private static final String TEST_USERNAME = "testUser";

    @BeforeEach
    void setUp() {
        openMocks(this);
        jwtService = new JwtServiceImpl();
        when(userDetails.getUsername()).thenReturn(TEST_USERNAME);
    }

    @Test
    void testGenerateToken() {
        String token = jwtService.generateToken(userDetails);
        assertNotNull(token);
        String username = jwtService.extractUsername(token);
        assertEquals(TEST_USERNAME, username);
    }

    @Test
    void testGenerateRefreshToken() {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", "USER");
        String refreshToken = jwtService.generateRefreshToken(extraClaims, userDetails);
        assertNotNull(refreshToken);
        String username = jwtService.extractUsername(refreshToken);
        assertEquals(TEST_USERNAME, username);
    }

    @Test
    void testExtractUsername() {
        String token = jwtService.generateToken(userDetails);
        String username = jwtService.extractUsername(token);
        assertEquals(TEST_USERNAME, username);
    }

    @Test
    void testIsTokenValid() {
        String token = jwtService.generateToken(userDetails);
        assertTrue(jwtService.isTokenValid(token, userDetails));
    }

    @Test
    void testIsTokenExpired() {
        String token = Jwts.builder()
                .subject(TEST_USERNAME)
                .issuedAt(new Date(System.currentTimeMillis() - 1000 * 60 * 60 * 24)) // 24 hours ago
                .expiration(new Date(System.currentTimeMillis() - 1000 * 60 * 60)) // 1 hour ago
                .signWith(jwtService.getSecretKey())
                .compact();
        assertTrue(jwtService.isTokenExpired(token));
    }
}
