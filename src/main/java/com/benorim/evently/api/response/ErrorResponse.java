package com.benorim.evently.api.response;

import com.benorim.evently.enums.ErrorState;

public record ErrorResponse(String error, ErrorState errorState) {
}
