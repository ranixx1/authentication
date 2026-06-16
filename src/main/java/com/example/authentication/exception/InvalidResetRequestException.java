// exception/InvalidResetRequestException.java
package com.example.authentication.exception;

public class InvalidResetRequestException extends RuntimeException {
    public InvalidResetRequestException(String message) {
        super(message);
    }
}