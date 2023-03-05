package com.application.banking.exception;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class InvalidInputException implements ExceptionHandler{
    public String message = null;
    public int status = 0;

    @Override
    public void setMessage(String message, int status) {
        this.message = message;
        this.status = status;
    }
}
