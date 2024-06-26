package com.example.kafkaService.orderService.handlers;

import com.example.kafkaService.orderService.exceptions.NotFoundOrderException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class OrderControllerHandler {
    @ExceptionHandler(NotFoundOrderException.class)
    public ResponseEntity<?> handleNotFoundOrderExceptionHandlers(NotFoundOrderException exception){
        return ResponseEntity.badRequest().body(exception.getMessage());
    }
}
