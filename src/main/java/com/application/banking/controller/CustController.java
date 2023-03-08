package com.application.banking.controller;

import com.application.banking.exception.InvalidInputException;
import com.application.banking.model.AccBalance;
import com.application.banking.model.CustAccMap;
import com.application.banking.model.CustDetails;
import com.application.banking.model.request.AccTransactionsReq;
import com.application.banking.model.request.CustCredentialsReq;
import com.application.banking.model.request.CustDetailsReq;
import com.application.banking.model.response.IResponse;
import com.application.banking.repository.AccBalanceRepo;
import com.application.banking.repository.CustAccMapRepo;
import com.application.banking.service.IBankService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/hctbank")
public class CustController {

    private IBankService iBankService;
    private AccBalanceRepo accBalanceRepo;
    private CustAccMapRepo custAccMapRepo;

    public CustController(IBankService iBankService, AccBalanceRepo accBalanceRepo, CustAccMapRepo custAccMapRepo) {
        this.iBankService = iBankService;
        this.accBalanceRepo = accBalanceRepo;
        this.custAccMapRepo = custAccMapRepo;
    }

    @PostMapping("/customers")
    public ResponseEntity<IResponse> saveCustomer(@Valid @RequestBody CustDetailsReq custDetailsReq){
        IResponse iResponse = iBankService.saveCustomerDetails(custDetailsReq);
        return new ResponseEntity<IResponse>(iResponse != null ? iResponse : (IResponse) new InvalidInputException("Invalid Details", HttpStatus.BAD_REQUEST.value()), HttpStatus.CREATED);
    }

    @PostMapping("/password")
    public ResponseEntity<String> setPassword(@Valid @RequestBody CustCredentialsReq custCredentialsReq){
        String response = iBankService.savePassword(custCredentialsReq);
        return new ResponseEntity<String>((response != null ? response: new InvalidInputException("Invalid Password",HttpStatus.BAD_REQUEST.value())).toString(),HttpStatus.CREATED);
    }

    @GetMapping("/balances")
    public ResponseEntity<String> getBalances(@RequestParam Long custId, @RequestParam Long accId){
        Optional<AccBalance> response = iBankService.getBalances(custId,accId);
        return new ResponseEntity<String>((response != null ? response: new InvalidInputException("Invalid Details",HttpStatus.BAD_REQUEST.value())).toString(),HttpStatus.CREATED);
    }

    @PostMapping("/transactions")
    public ResponseEntity<String> saveTransaction(@Valid @RequestBody AccTransactionsReq accTransactionsReq){
        Long response = iBankService.saveTransaction(accTransactionsReq);
        return new ResponseEntity<String>((response != null ? response: new InvalidInputException("Invalid Details",HttpStatus.BAD_REQUEST.value())).toString(),HttpStatus.CREATED);
    }
    @GetMapping("/customers")
    public ResponseEntity<String> getCustomerDetails(@RequestParam Long custId){
        Optional<CustDetails> response = iBankService.getCustomerDetails(custId);
        return new ResponseEntity<String>((response != null ? response: new InvalidInputException("Invalid Details",HttpStatus.BAD_REQUEST.value())).toString(),HttpStatus.CREATED);
    }
}
