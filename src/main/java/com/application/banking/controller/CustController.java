package com.application.banking.controller;

import com.application.banking.exception.InvalidInputException;
import com.application.banking.model.AccBalance;
import com.application.banking.model.AccTransactions;
import com.application.banking.model.request.AccTransactionsReq;
import com.application.banking.model.request.CustCredentialsReq;
import com.application.banking.model.request.CustDetailsReq;
import com.application.banking.model.response.IResponse;
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

    public CustController(IBankService iBankService) {
        this.iBankService = iBankService;
    }

    @PostMapping("/customers")
    public String saveCustomer(@Valid @RequestBody CustDetailsReq custDetailsReq) {
        String response = iBankService.saveCustomerDetails(custDetailsReq);
        return response;
    }

    @PostMapping("/password")
    public String setPassword(@Valid @RequestBody CustCredentialsReq custCredentialsReq) {
        String response = iBankService.savePassword(custCredentialsReq);
        return response;
    }

    @GetMapping("/balances")
    public Object getBalances(@RequestParam(name = "custId", required = false) Long custId, @RequestParam(name = "accId", required = false) Long accId) {
        if (custId == null && accId == null) {
            return "Both CustomerId and AccountId cannot be zero. Please give valid details.";
        } else {
            return iBankService.getBalances(custId, accId);
        }
    }
    @PostMapping("/transactions")
    public String saveTransaction(@Valid @RequestBody AccTransactionsReq accTransactionsReq) {
        String response = iBankService.saveTransactions(accTransactionsReq);
        return response;
    }

    @GetMapping("/transactions")
    public Object getTransactions(@RequestParam(name = "accId", required = false) Long accId, @RequestParam(name = "transactionRefId", required = false) Long transactionRefId) {
        if (accId != null && transactionRefId == null) {
            return iBankService.getTransactionsByAccId(accId);
        }
        if (accId == null && transactionRefId != null) {
            return iBankService.getTransactionsByTransactionRefId(transactionRefId);
        }
        if (accId != null && transactionRefId != null) {
            return iBankService.getTransactionsByBoth(accId, transactionRefId);
        }
        if (accId == null && transactionRefId == null) {
            return "Transaction ReferenceId and AccountId cannot be null. Please give valid details.";
        }
        return null;
    }

    @GetMapping("/customers")
    public Object getCustomerDetails(@RequestParam(name = "custId", required = false) Long custId) {
        if (custId == null) {
            return iBankService.getCustomerDetails();
        } else {
            return iBankService.getCustomerDetails(custId);
        }
    }
}
