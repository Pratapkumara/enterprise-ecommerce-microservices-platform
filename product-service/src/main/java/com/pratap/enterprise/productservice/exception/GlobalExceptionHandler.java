package com.pratap.enterprise.productservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String TIMESTAMP_KEY =
            "timestamp";

    private static final String STATUS_KEY =
            "status";

    private static final String ERROR_KEY =
            "error";

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Map<String, Object>>
            handleProductNotFound(
                    ProductNotFoundException exception) {

        Map<String, Object> response =
                new HashMap<>();

        response.put(
                TIMESTAMP_KEY,
                LocalDateTime.now()
        );

        response.put(
                STATUS_KEY,
                HttpStatus.NOT_FOUND.value()
        );

        response.put(
                ERROR_KEY,
                exception.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>>
            handleValidation(
                    MethodArgumentNotValidException exception) {

        Map<String, Object> response =
                new HashMap<>();

        response.put(
                TIMESTAMP_KEY,
                LocalDateTime.now()
        );

        response.put(
                STATUS_KEY,
                HttpStatus.BAD_REQUEST.value()
        );

        Map<String, String> errors =
                new HashMap<>();

        exception.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        errors.put(
                                error.getField(),
                                error.getDefaultMessage()
                        )
                );

        response.put("errors", errors);

        return ResponseEntity
                .badRequest()
                .body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>>
            handleException(Exception exception) {

        Map<String, Object> response =
                new HashMap<>();

        response.put(
                TIMESTAMP_KEY,
                LocalDateTime.now()
        );

        response.put(
                STATUS_KEY,
                HttpStatus.INTERNAL_SERVER_ERROR.value()
        );

        response.put(
                ERROR_KEY,
                exception.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }
}
