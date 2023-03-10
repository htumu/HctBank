package com.application.banking.service;

import com.application.banking.model.AccBalance;
import com.application.banking.model.CustAccMap;
import com.application.banking.model.CustDetails;
import com.application.banking.model.request.*;

import java.util.List;
import java.util.Optional;

public interface IBankService {
    String saveCustomerDetails(CustDetailsReq custDetailsReq);
    Long saveCustomerAddress(CustAddressReq custAddressReq);
    Long updateAccount(AccBalanceReq accBalanceReq);
    String saveTransactions(AccTransactionsReq accTransactionsReq);
    public boolean mapAccIdToCustId(CustAccMap custAccMap);
    public String savePassword(CustCredentialsReq custCredentialsReq);
    Optional<AccBalance> getBalances(Long custId, Long accId);
    Optional<CustDetails> getCustomerDetails(Long custId);
    List<CustDetails> getCustomerDetails();
    Object getTransactionsByBoth(Long accId, Long transactionRefId);
    Object getTransactionsByAccId(Long accId);
    Object getTransactionsByTransactionRefId(Long transactionRefId);


}
