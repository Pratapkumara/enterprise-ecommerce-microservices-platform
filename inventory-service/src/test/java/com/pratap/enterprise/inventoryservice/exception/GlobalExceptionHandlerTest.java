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

        ResponseEntity<Map<String, Object>> response =
                handler.handleInventoryNotFound(exception);

        assertEquals(
                HttpStatus.NOT_FOUND,
                response.getStatusCode()
        );

        Map<String, Object> body =
                response.getBody();

        assertNotNull(body);
        assertEquals(
                "Inventory not found",
                body.get("error")
        );
        assertEquals(404, body.get("status"));
        assertNotNull(body.get("timestamp"));
    }

    @Test
    void stockOperationShouldReturn400Response() {
        StockOperationException exception =
                new StockOperationException(
                        "Insufficient stock available"
                );

        ResponseEntity<Map<String, Object>> response =
                handler.handleStockOperation(exception);

        assertEquals(
                HttpStatus.BAD_REQUEST,
                response.getStatusCode()
        );

        Map<String, Object> body =
                response.getBody();

        assertNotNull(body);
        assertEquals(
                "Insufficient stock available",
                body.get("error")
        );
        assertEquals(400, body.get("status"));
        assertNotNull(body.get("timestamp"));
    }

    @Test
    void unexpectedRuntimeExceptionShouldReturn400Response() {
        RuntimeException exception =
                new RuntimeException(
                        "Unexpected inventory failure"
                );

        ResponseEntity<Map<String, Object>> response =
                handler.handleRuntimeException(exception);

        assertEquals(
                HttpStatus.BAD_REQUEST,
                response.getStatusCode()
        );

        Map<String, Object> body =
                response.getBody();

        assertNotNull(body);
        assertEquals(
                "Unexpected inventory failure",
                body.get("error")
        );
        assertEquals(400, body.get("status"));
        assertNotNull(body.get("timestamp"));
    }
}
