package com.application.banking.controller;

import com.application.banking.exception.EntityNotFoundException;
import com.application.banking.exception.InvalidInputException;
import com.application.banking.exception.NoRecordFoundException;
import com.application.banking.util.HCStatusCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RestController
public class CustomizedExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(EntityNotFoundException.class)
    public final ResponseEntity<InvalidInputException> handleNotFoundException(EntityNotFoundException ex, WebRequest request) {
        InvalidInputException invalidInputException = new InvalidInputException(ex.getMessage(), HCStatusCode.HCTB400);
        return new ResponseEntity<InvalidInputException>(invalidInputException, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(NoRecordFoundException.class)
    public final ResponseEntity<InvalidInputException> noRecordFoundException(NoRecordFoundException ex, WebRequest request){
        InvalidInputException invalidInputException = new InvalidInputException(ex.getMessage(), HCStatusCode.HCTB404);
        return new ResponseEntity<InvalidInputException>(invalidInputException, HttpStatus.NOT_FOUND);
    }

}
