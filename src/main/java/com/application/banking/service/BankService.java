package com.application.banking.service;

import com.application.banking.model.CustAccMap;
import com.application.banking.model.request.AccBalanceReq;
import com.application.banking.model.request.CustAddressReq;
import com.application.banking.model.request.CustCredentialsReq;
import com.application.banking.model.request.CustDetailsReq;
import com.application.banking.model.response.Response;

public interface BankService {
    Response saveCustomerDetails(CustDetailsReq custDetailsReq);
    Long saveCustomerAddress(CustAddressReq custAddressReq);
    Long updateAccount(AccBalanceReq accBalanceReq);
    boolean mapAccIdToCustId(CustAccMap custAccMap);
    String savePassword(CustCredentialsReq custCredentialsReq);
}
