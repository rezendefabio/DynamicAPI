package com.example.dynamicapi.exception;

public class CustomException extends RuntimeException {
    public CustomException(String message) {
        super(message);
    }
}