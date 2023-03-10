package com.application.banking.service;

import com.application.banking.model.AccTransactions;
import com.application.banking.model.CustAccMap;
import com.application.banking.model.request.*;
import com.application.banking.model.response.IResponse;

import java.util.Optional;

public interface IBankService {
    IResponse saveCustomerDetails(CustDetailsReq custDetailsReq);
    Long saveCustomerAddress(CustAddressReq custAddressReq);
    Long updateAccount(AccBalanceReq accBalanceReq);
    String saveTransactions(AccTransactionsReq accTransactionsReq);
    public boolean mapAccIdToCustId(CustAccMap custAccMap);
    public String savePassword(CustCredentialsReq custCredentialsReq);
    Object getBalances(Long custId, Long accId);
    Object getCustomerDetails(Long custId);
    Object getTransactions(Long accId, Long transactionRefId);


}
