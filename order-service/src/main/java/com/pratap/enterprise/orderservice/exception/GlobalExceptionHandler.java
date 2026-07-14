package com.pratap.enterprise.orderservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;


@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<?> handleOrderNotFound(
            OrderNotFoundException exception
    ) {


        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                        new ErrorResponse(
                                exception.getMessage(),
                                LocalDateTime.now()
                        )
                );
    }



    record ErrorResponse(
            String message,
            LocalDateTime timestamp
    ) {}

}
