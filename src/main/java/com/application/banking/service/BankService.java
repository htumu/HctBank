package com.application.banking.service;

import com.application.banking.model.CustAccMap;
import com.application.banking.model.request.*;
import com.application.banking.model.response.Response;

public interface BankService {
    Response saveCustomerDetails(CustDetailsReq custDetailsReq);
    Long saveCustomerAddress(CustAddressReq custAddressReq);
    Long updateAccount(AccBalanceReq accBalanceReq);
    Long saveTransaction(AccTransactionsReq accTransactionsReq);
    public boolean mapAccIdToCustId(CustAccMap custAccMap);
    public String savePassword(CustCredentialsReq custCredentialsReq);
    Long creditAmount(AccTransactionsReq accTransactionsReq);
    Long debitAmount(AccTransactionsReq accTransactionsReq);
}
