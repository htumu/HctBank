package com.application.banking.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NoRecordFoundException extends RuntimeException{
    public NoRecordFoundException(String message){
        super(message);
    }
}
