package com.meditrack.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Map;
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> handleResponseStatus(
            ResponseStatusException ex) {

        String message = ex.getReason() != null
                ? ex.getReason()
                : "Error inesperado";

        Map<String, Object> response = Map.of(
                "error", message,
                "status", ex.getStatusCode().value(),
                "timestamp", LocalDateTime.now()
        );

        return ResponseEntity
                .status(ex.getStatusCode())
                .body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric() {

        Map<String, Object> response = Map.of(
                "error", "Error interno del servidor",
                "status", HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "timestamp", LocalDateTime.now()
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }
}