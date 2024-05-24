package com.benorim.evently.api.response;

import com.benorim.evently.enums.Rsvp;

import java.time.LocalDate;

public record InvitationResponse (
    Long id,
    String email,
    String firstName,
    String lastName,
    String phone,
    Rsvp rsvp,
    EventResponse event,
    LocalDate dateSent,
    LocalDate rsvpByDate,
    LocalDate dateRsvped
) {}
