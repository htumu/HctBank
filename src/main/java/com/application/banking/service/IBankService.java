package com.application.banking.service;

import com.application.banking.model.AccBalance;
import com.application.banking.model.CustAccMap;
import com.application.banking.model.CustDetails;
import com.application.banking.model.request.*;
import com.application.banking.model.response.IResponse;

import java.util.Optional;

public interface IBankService {
    IResponse saveCustomerDetails(CustDetailsReq custDetailsReq);
    Long saveCustomerAddress(CustAddressReq custAddressReq);
    Long updateAccount(AccBalanceReq accBalanceReq);
    Long saveTransaction(AccTransactionsReq accTransactionsReq);
    public boolean mapAccIdToCustId(CustAccMap custAccMap);
    public String savePassword(CustCredentialsReq custCredentialsReq);
    Optional<AccBalance> getBalances(Long custId, Long accId);
    Optional<CustDetails> getCustomerDetails(Long custId);


}
