package com.benorim.evently.api.request;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public record EventCreateRequest(
        @NotBlank String title,
        String description,
        String addressLine1,
        String addressLine2,
        String city,
        String state,
        String zipCode,
        String imageUrl,
        LocalDateTime startTime,
        LocalDateTime endTime,
        String additionalNotes,
        Boolean isPrivate
        ) {
}
