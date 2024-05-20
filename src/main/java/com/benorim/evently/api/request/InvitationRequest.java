package com.benorim.evently.api.request;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public record InvitationRequest(
        @NotBlank String email,
        String phone,
        String firstName,
        String lastName,
        Long eventId,
        LocalDate rsvpByDate
) {
}
