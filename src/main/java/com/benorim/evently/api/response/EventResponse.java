package com.benorim.evently.api.response;

import java.time.LocalDateTime;

public record EventResponse(
    Long id,
    String title,
    String description,
    String addressLine1,
    String addressLine2,
    String city,
    String state,
    String zipCode,
    String imageUrl,
    LocalDateTime startTime,
    LocalDateTime endTime,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    String additionalNotes,
    Boolean isPrivate
) {
}
