package com.application.banking.exception;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class InvalidInputException implements IExceptionHandler {
    public String message = null;
    public String reasonCode = null;

    public void setMessage(String message, String reasonCode) {
        this.message = message;
        this.reasonCode = reasonCode;
    }
}
