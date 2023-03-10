package com.application.banking.controller;

import com.application.banking.exception.InvalidInputException;
import com.application.banking.model.request.AccTransactionsReq;
import com.application.banking.model.request.CustCredentialsReq;
import com.application.banking.model.request.CustDetailsReq;
import com.application.banking.model.response.IResponse;
import com.application.banking.service.IBankService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/hctbank")
public class CustController {

    private IBankService iBankService;
    public CustController(IBankService iBankService) {
        this.iBankService = iBankService;
    }

    @PostMapping("/customers")
    public ResponseEntity<IResponse> saveCustomer(@Valid @RequestBody CustDetailsReq custDetailsReq) {
        IResponse iResponse = iBankService.saveCustomerDetails(custDetailsReq);
        return new ResponseEntity<IResponse>(iResponse != null ? iResponse : (IResponse) new InvalidInputException("Invalid Details", HttpStatus.BAD_REQUEST.value()), HttpStatus.CREATED);
    }

    @PostMapping("/password")
    public ResponseEntity<String> setPassword(@Valid @RequestBody CustCredentialsReq custCredentialsReq) {
        String response = iBankService.savePassword(custCredentialsReq);
        return new ResponseEntity<String>((response != null ? response : new InvalidInputException("Invalid Password", HttpStatus.BAD_REQUEST.value())).toString(), HttpStatus.CREATED);
    }

    @GetMapping("/balances")
    public Object getBalances(@RequestParam Long custId, @RequestParam Long accId) {
        Object response = iBankService.getBalances(custId, accId);
        return response;
    }

    @PostMapping("/transactions")
    public ResponseEntity<String> saveTransaction(@Valid @RequestBody AccTransactionsReq accTransactionsReq) {
        String response = iBankService.saveTransactions(accTransactionsReq);
        return new ResponseEntity<String>((response != null ? response : new InvalidInputException("Invalid Details", HttpStatus.BAD_REQUEST.value())).toString(), HttpStatus.CREATED);
    }
    @GetMapping("/transactions")
    public Object getTransactions(@RequestParam Long accId, @RequestParam Long transactionRefId){
        Object response = iBankService.getTransactions(accId, transactionRefId);
        return response;
    }

    @GetMapping("/customers")
    public Object getCustomerDetails(@RequestParam Long custId) {
        Object response = iBankService.getCustomerDetails(custId);
        return response;
    }
}
