package org.ikitadevs.authservice.advice;


import org.ikitadevs.authservice.dto.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalApiExceptionHandler {
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        ErrorResponse errorResponse = ErrorResponse.builder()
                .error("Unauthorized")
                .message(ex.getMessage())
                .timestamp(Instant.now())
                .build();
        return new ResponseEntity<>(errorResponse, status);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponse> handleResponseStatusException(ResponseStatusException ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .error(ex.getStatusCode().toString())
                .message(ex.getReason())
                .timestamp(Instant.now())
                .build();
        return new ResponseEntity<>(errorResponse, ex.getStatusCode());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorResponse errorResponse = ErrorResponse.builder()
                .error(status.toString())
                .message("Incorrect fields value!")
                .timestamp(Instant.now())
                .build();
        return new ResponseEntity<>(errorResponse, status);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(BadCredentialsException ex) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        ErrorResponse errorResponse = ErrorResponse.builder()
                .error(status.toString())
                .message(ex.getMessage())
                .timestamp(Instant.now())
                .build();
        return new ResponseEntity<>(errorResponse, status);
    }

    }
