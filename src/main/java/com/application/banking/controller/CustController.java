package com.application.banking.controller;

import com.application.banking.exception.NoRecordFoundException;
import com.application.banking.model.request.AccTransactionsReq;
import com.application.banking.model.request.CustCredentialsReq;
import com.application.banking.model.request.CustDetailsReq;
import com.application.banking.model.response.*;
import com.application.banking.repository.CustCredentialsRepo;
import com.application.banking.service.IBankService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/hctbank")
public class CustController {
    Logger logger = LoggerFactory.getLogger(CustController.class);


    private IBankService iBankService;
    private CustCredentialsRepo custCredentialsRepo;

    public CustController(IBankService iBankService, CustCredentialsRepo custCredentialsRepo) {
        this.iBankService = iBankService;
        this.custCredentialsRepo = custCredentialsRepo;
    }

    @PostMapping("/customers")
    public ResponseEntity<CreateCustResponse> saveCustomer(@Valid @RequestBody CustDetailsReq custDetailsReq) {
        CreateCustResponse response = iBankService.saveCustomerDetails(custDetailsReq);
        if (response == null) {
            throw new NoRecordFoundException("Invalid Customer Details.");
        }
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/password")
    public ResponseEntity<?> setPassword(@Valid @RequestBody CustCredentialsReq custCredentialsReq) {
        if(custCredentialsRepo.findById(custCredentialsReq.getCustId()).isEmpty()) {
            String response = iBankService.savePassword(custCredentialsReq);
            if (response == null) {
                throw new NoRecordFoundException("Invalid Details.");
            }
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }
        else{
            String response = iBankService.updatePassword(custCredentialsReq);
            if (response == null){
                throw new NoRecordFoundException("Invalid Details.");
            }
            return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping("/balances")
    public Object getBalances(@RequestParam(name = "custId", required = false) Long custId, @RequestParam(name = "accId", required = false) Long accId) {
        if (custId == null && accId == null) {
            throw new NoRecordFoundException("Both CustomerId and AccountId cannot be null. Please give valid details.");
        } else {
            return iBankService.getBalances(custId, accId);
        }
    }

    @PostMapping("/transactions")
    public ResponseEntity<?> saveTransaction(@Valid @RequestBody AccTransactionsReq accTransactionsReq) {
        Object response = iBankService.saveTransactions(accTransactionsReq);
        if(response == null) {
            throw new NoRecordFoundException("Invalid Details.");
        }
        return new ResponseEntity<>(response, HttpStatus.CREATED);

    }

    @GetMapping("/transactions")
    public ResponseEntity<IResponse> getTransactions(@RequestParam(name = "accId", required = false) Long accId, @RequestParam(name = "transactionRefId", required = false) Long transactionRefId) {
       GetTransactionResponses response = iBankService.getTransactionResponses(accId,transactionRefId);
       return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/customers")
    public ResponseEntity<IResponse> getCustomerDetails(@RequestParam(name = "custId", required = false) Long custId) {
        GetCustResponses response = iBankService.getCustomerDetails(custId);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
}
