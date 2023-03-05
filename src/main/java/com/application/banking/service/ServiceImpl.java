package com.application.banking.service;

import com.application.banking.generateId.IdGenerator;
import com.application.banking.model.*;
import com.application.banking.model.request.AccBalanceReq;
import com.application.banking.model.request.CustAddressReq;
import com.application.banking.model.request.CustCredentialsReq;
import com.application.banking.model.request.CustDetailsReq;
import com.application.banking.model.response.CreateResponse;
import com.application.banking.model.response.Response;
import com.application.banking.repository.*;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;

@Service
public class ServiceImpl implements BankService{
    private static final int ADDRESS_ID_LENGTH = 6;
    private static final int CUSTOMER_ID_LENGTH = 6;
    private static final int ACCOUNT_ID_LENGTH = 12;

    private AccBalanceRepo accBalanceRepo;
    private AccTransactionsRepo accTransactionsRepo;
    private CustAccMapRepo custAccMapRepo;
    private CustAddressRepo custAddressRepo;
    private CustCredentialsRepo custCredentialsRepo;
    private CustDetailsRepo custDetailsRepo;

    public ServiceImpl(AccBalanceRepo accBalanceRepo, AccTransactionsRepo accTransactionsRepo, CustAccMapRepo custAccMapRepo, CustAddressRepo custAddressRepo, CustCredentialsRepo custCredentialsRepo, CustDetailsRepo custDetailsRepo) {
        this.accBalanceRepo = accBalanceRepo;
        this.accTransactionsRepo = accTransactionsRepo;
        this.custAccMapRepo = custAccMapRepo;
        this.custAddressRepo = custAddressRepo;
        this.custCredentialsRepo = custCredentialsRepo;
        this.custDetailsRepo = custDetailsRepo;
    }

    @Override
    public Response saveCustomerDetails(CustDetailsReq custDetailsReq) {
        Long addressId = saveCustomerAddress(custDetailsReq.getAddress());
        CustDetails custDetails = new CustDetails();

        custDetails.setCustId(IdGenerator.generateRandom(CUSTOMER_ID_LENGTH));
        custDetails.setName(custDetailsReq.getName());
        custDetails.setPhone(custDetailsReq.getPhone());
        custDetails.setEmail(custDetailsReq.getEmail());
        custDetails.setAddressId(addressId);

        Timestamp timestamp = Timestamp.from(Instant.now());
        custDetails.setCreated(timestamp);
        custDetails.setLastUpdated(timestamp);

        Long custId = custDetailsRepo.save(custDetails).getCustId();
        Long accId = updateAccount(null);

        boolean mapResult = mapAccIdToCustId(new CustAccMap(accId, custId));
        return mapResult ? new CreateResponse(custId) : null;
    }

    @Override
    public Long saveCustomerAddress(CustAddressReq custAddressReq) {
        CustAddress custAddress = new CustAddress();

        custAddress.setAddressId(IdGenerator.generateRandom(ADDRESS_ID_LENGTH));
        custAddress.setCountry(custAddressReq.getCountry());
        custAddress.setCity(custAddressReq.getCity());
        custAddress.setAddressLane(custAddressReq.getAddressLane());
        custAddress.setPin(custAddressReq.getPin());
        custAddress.setLastUpdated(Timestamp.from(Instant.now()));

        return custAddressRepo.save(custAddress).getAddressId();
    }

    @Override
    public Long updateAccount(AccBalanceReq accBalanceReq) {
        AccBalance accBal = new AccBalance();

        if (accBalanceReq == null) {
            accBal.setAccId(IdGenerator.generateRandom(ACCOUNT_ID_LENGTH));
            accBal.setBalance(500.00);
        } else {
            accBal.setAccId(accBal.getAccId());
            accBal.setBalance(accBalanceReq.getBalance());
        }

        return accBalanceRepo.save(accBal).getAccId();
    }

    @Override
    public boolean mapAccIdToCustId(CustAccMap custAccMap) {
        CustAccMap map = custAccMapRepo.save(custAccMap);
        return map != null ? true : false;
    }

    @Override
    public String savePassword(CustCredentialsReq custCredentialsReq) {
        CustCredentials custCredentials = new CustCredentials();

        custCredentials.setCustId(custCredentialsReq.getCustId());
        custCredentials.setPassword(custCredentialsReq.getPassword());
        custCredentialsRepo.save(custCredentials);
        return "details are saved for : "+custCredentials.getCustId();
    }
}
