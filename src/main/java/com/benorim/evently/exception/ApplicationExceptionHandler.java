package com.benorim.evently.exception;

import com.benorim.evently.api.response.ErrorResponse;
import com.benorim.evently.enums.ErrorState;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ApplicationExceptionHandler {

    @ExceptionHandler(EventUpdateOrCreateException.class)
    @ResponseStatus(value= HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleEventCreateOrUpdateException(EventUpdateOrCreateException exception) {
        return new ResponseEntity<>(new ErrorResponse(exception.getMessage(), ErrorState.CREATE_OR_UPDATE_FAILURE), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalOperationException.class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public ResponseEntity<ErrorResponse> handleIllegalOperationException(IllegalOperationException exception) {
        return new ResponseEntity<>(new ErrorResponse(exception.getMessage(), ErrorState.DELETE_FAILURE), HttpStatus.FORBIDDEN);
    }
}
