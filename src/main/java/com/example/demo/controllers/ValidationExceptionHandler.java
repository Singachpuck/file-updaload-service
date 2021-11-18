package com.example.demo.controllers;

import com.example.demo.exceptions.WrongPathVariableException;
import com.example.demo.exceptions.WrongRequestBodyException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;

@ControllerAdvice
public class ValidationExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody
    Map<String, Object> validationErrorResponse(MethodArgumentNotValidException e) {
        return Map.of("success", false, "error", e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
    }

    @ExceptionHandler(WrongPathVariableException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public @ResponseBody
    Map<String, Object> deleteErrorResponse(WrongPathVariableException e) {
        return Map.of("success", false, "error", e.getMessage());
    }

    @ExceptionHandler(WrongRequestBodyException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody
    Map<String, Object> removeTagsException(WrongRequestBodyException e) {
        return Map.of("success", false, "error", e.getMessage());
    }
}
