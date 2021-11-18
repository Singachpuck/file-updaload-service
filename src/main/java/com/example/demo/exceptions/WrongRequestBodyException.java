package com.example.demo.exceptions;

public class WrongRequestBodyException extends RuntimeException {

    public WrongRequestBodyException() {
        super();
    }

    public WrongRequestBodyException(String message, Throwable cause) {
        super(message, cause);
    }

    public WrongRequestBodyException(String message) {
        super(message);
    }

    public WrongRequestBodyException(Throwable cause) {
        super(cause);
    }
}
