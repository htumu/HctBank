package com.application.banking.service;

import com.application.banking.model.AccBalance;
import com.application.banking.model.CustAccMap;
import com.application.banking.model.CustDetails;
import com.application.banking.model.request.*;
import com.application.banking.model.response.CreateCustResponse;
import com.application.banking.model.response.GetCustResponses;
import com.application.banking.model.response.GetTransactionResponses;

import java.util.List;
import java.util.Optional;

public interface IBankService {
    CreateCustResponse saveCustomerDetails(CustDetailsReq custDetailsReq);
    Long saveCustomerAddress(CustAddressReq custAddressReq);
    Long updateAccount(AccBalanceReq accBalanceReq);
    Object saveTransactions(AccTransactionsReq accTransactionsReq);
    public boolean mapAccIdToCustId(CustAccMap custAccMap);
    public String savePassword(CustCredentialsReq custCredentialsReq);
    public String updatePassword(CustCredentialsReq custCredentialsReq);
    Optional<AccBalance> getBalances(Long custId, Long accId);
    GetCustResponses getCustomerDetails(Long custId);
    public GetTransactionResponses getTransactionResponses(Long accId, Long transactionRefId);


}
