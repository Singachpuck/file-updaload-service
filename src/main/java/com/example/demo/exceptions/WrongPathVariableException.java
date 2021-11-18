package com.example.demo.exceptions;

public class WrongPathVariableException extends RuntimeException {

    public WrongPathVariableException() {
        super();
    }

    public WrongPathVariableException(String message, Throwable cause) {
        super(message, cause);
    }

    public WrongPathVariableException(String message) {
        super(message);
    }

    public WrongPathVariableException(Throwable cause) {
        super(cause);
    }
}
