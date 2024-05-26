package com.benorim.evently.api.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record InvitationRequest(
        @NotBlank String email,
        String phone,
        String firstName,
        String lastName,
        @NotNull Long eventId,
        LocalDate rsvpByDate
) {
}
