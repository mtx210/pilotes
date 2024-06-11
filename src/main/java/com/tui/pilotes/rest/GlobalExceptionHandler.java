package com.tui.pilotes.rest;

import com.tui.pilotes.exception.OrderUpdateTimedOutException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(OrderUpdateTimedOutException.class)
    public ResponseEntity<String> handleOrderUpdateTimedOutException(OrderUpdateTimedOutException exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_ACCEPTABLE)
                .body(exception.getMessage());
    }
}