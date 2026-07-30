package com.pratap.enterprise.inventoryservice.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler =
            new GlobalExceptionHandler();

    @Test
    void inventoryNotFoundShouldReturn404Response() {
        InventoryNotFoundException exception =
                new InventoryNotFoundException(
                        "Inventory not found"
                );

        ResponseEntity<?> response =
                handler.handleInventoryNotFound(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());

        Map<?, ?> body = (Map<?, ?>) response.getBody();

        assertEquals("Inventory not found", body.get("error"));
        assertEquals(404, body.get("status"));
        assertNotNull(body.get("timestamp"));
    }

    @Test
    void runtimeExceptionShouldReturn400Response() {
        RuntimeException exception =
                new RuntimeException("Insufficient stock");

        ResponseEntity<?> response =
                handler.handleRuntimeException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());

        Map<?, ?> body = (Map<?, ?>) response.getBody();

        assertEquals("Insufficient stock", body.get("error"));
        assertEquals(400, body.get("status"));
        assertNotNull(body.get("timestamp"));
    }
}
