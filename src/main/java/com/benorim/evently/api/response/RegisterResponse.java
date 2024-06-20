package com.benorim.evently.api.response;

import com.benorim.evently.enums.Role;

public record RegisterResponse(Long id, String email, Role role, String organizationName, String organizationEmail) {
}
