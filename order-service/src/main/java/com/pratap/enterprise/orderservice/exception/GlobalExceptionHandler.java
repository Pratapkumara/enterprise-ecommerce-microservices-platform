package com.pratap.enterprise.orderservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ErrorResponse>
            handleOrderNotFound(
                    OrderNotFoundException exception) {

        ErrorResponse body =
                new ErrorResponse(
                        exception.getMessage(),
                        LocalDateTime.now()
                );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(body);
    }

    record ErrorResponse(
            String message,
            LocalDateTime timestamp) {
    }
}
