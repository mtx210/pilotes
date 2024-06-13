package com.tui.pilotes.rest;

import com.tui.pilotes.exception.OrderUpdateTimedOutException;
import com.tui.pilotes.rest.dto.response.ApiErrorResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(OrderUpdateTimedOutException.class)
    public ResponseEntity<ApiErrorResponse> handleException(OrderUpdateTimedOutException exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_ACCEPTABLE)
                .body(new ApiErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> handleException(IllegalArgumentException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleException(MethodArgumentNotValidException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiErrorResponse(
                        Arrays.stream(Optional.ofNullable(exception.getDetailMessageArguments())
                                        .orElse(new String[]{"unknown api argument exception"}))
                                .map(Object::toString)
                                .filter(StringUtils::isNotBlank)
                                .collect(Collectors.joining())
                ));
    }
}