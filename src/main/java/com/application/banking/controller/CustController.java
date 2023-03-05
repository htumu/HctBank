package com.application.banking.controller;

import com.application.banking.exception.InvalidInputException;
import com.application.banking.model.*;
import com.application.banking.model.request.CustCredentialsReq;
import com.application.banking.model.request.CustDetailsReq;
import com.application.banking.model.response.Response;
import com.application.banking.service.BankService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/hctbank")
public class CustController {

    private BankService bankService;

    public CustController(BankService bankService) {
        this.bankService = bankService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> saveCustomer(@Valid @RequestBody CustDetailsReq custDetailsReq){
        Response response = bankService.saveCustomerDetails(custDetailsReq);
        return new ResponseEntity<String>((response != null ? response : new InvalidInputException("Invalid Customer Details",HttpStatus.BAD_REQUEST.value())).toString(),HttpStatus.CREATED);
    }

    @PostMapping("/password")
    public ResponseEntity<String> setPassword(@Valid @RequestBody CustCredentialsReq custCredentialsReq){
        String response = bankService.savePassword(custCredentialsReq);
        return new ResponseEntity<String>((response != null ? response: new InvalidInputException("Invalid Password",HttpStatus.BAD_REQUEST.value())).toString(),HttpStatus.CREATED);
    }

    @PostMapping("/transactions")
    public ResponseEntity<String> transactions(@RequestBody AccTransactionsReq accTransactionsReq){
        
    }

}
