package com.benorim.evently.api.response;

import java.util.List;

public record EventsResponse(List<EventResponse> events) {
}
