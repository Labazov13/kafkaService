package com.example.kafkaService.orderService.exceptions;

public class NotFoundOrderException extends RuntimeException{
    public NotFoundOrderException(String message) {
        super(message);
    }
}
