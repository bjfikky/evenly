package com.benorim.evently.api.response;

public record JwtResponse(String email, String token, String refreshToken) {
}
