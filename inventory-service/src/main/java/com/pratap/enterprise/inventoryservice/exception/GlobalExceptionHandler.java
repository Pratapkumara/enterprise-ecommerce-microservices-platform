package com.pratap.enterprise.inventoryservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String ERROR_KEY =
            "error";

    private static final String TIMESTAMP_KEY =
            "timestamp";

    private static final String STATUS_KEY =
            "status";

    @ExceptionHandler(InventoryNotFoundException.class)
    public ResponseEntity<Map<String, Object>>
            handleInventoryNotFound(
                    InventoryNotFoundException exception) {

        Map<String, Object> body =
                createErrorBody(
                        exception.getMessage(),
                        HttpStatus.NOT_FOUND
                );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(body);
    }

    @ExceptionHandler(StockOperationException.class)
    public ResponseEntity<Map<String, Object>>
            handleStockOperation(
                    StockOperationException exception) {

        Map<String, Object> body =
                createErrorBody(
                        exception.getMessage(),
                        HttpStatus.BAD_REQUEST
                );

        return ResponseEntity
                .badRequest()
                .body(body);
    }

    /*
     * Retained as a final fallback for unexpected runtime failures.
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>>
            handleRuntimeException(
                    RuntimeException exception) {

        Map<String, Object> body =
                createErrorBody(
                        exception.getMessage(),
                        HttpStatus.BAD_REQUEST
                );

        return ResponseEntity
                .badRequest()
                .body(body);
    }

    private Map<String, Object> createErrorBody(
            String message,
            HttpStatus status) {

        return Map.of(
                ERROR_KEY, message,
                TIMESTAMP_KEY, LocalDateTime.now(),
                STATUS_KEY, status.value()
        );
    }
}
