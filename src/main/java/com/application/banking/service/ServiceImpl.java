package com.application.banking.service;

import com.application.banking.generateId.IdGenerator;
import com.application.banking.model.*;
import com.application.banking.model.request.*;
import com.application.banking.model.response.CreateCustResponse;
import com.application.banking.model.response.GetCustDetailsResponse;
import com.application.banking.model.response.IResponse;
import com.application.banking.repository.*;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

@Service
public class ServiceImpl implements IBankService {
    private static final int ADDRESS_ID_LENGTH = 6;
    private static final int CUSTOMER_ID_LENGTH = 6;
    private static final int ACCOUNT_ID_LENGTH = 12;
    private static final int TRANSACTION_ID_LENGTH = 8;

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
    public IResponse saveCustomerDetails(CustDetailsReq custDetailsReq) {
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
        return mapResult ? new CreateCustResponse(custId) : null;
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
        AccBalance accBalance = new AccBalance();

        if (accBalanceReq == null) {
            accBalance.setAccId(IdGenerator.generateRandom(ACCOUNT_ID_LENGTH));
            accBalance.setBalance(500.00);
        } else {
            accBalance.setAccId(accBalance.getAccId());
            accBalance.setBalance(accBalanceReq.getBalance());
        }

        return accBalanceRepo.save(accBalance).getAccId();
    }

    @Override
    public Long saveTransaction(AccTransactionsReq accTransactionsReq) {
        Long fromAccId = accTransactionsReq.getFromAccId();
        Long toAccId = accTransactionsReq.getToAccId();
        AccTransactions accTransactions = new AccTransactions();
        AccBalance accBalance = new AccBalance();
        double fromAccBalance = accBalanceRepo.findBalanceByAccId(fromAccId);
        double toAccBalance = accBalanceRepo.findBalanceByAccId(toAccId);

        if(accTransactionsReq.getType()=="CREDIT" && fromAccBalance>=accTransactionsReq.getAmount()){


            // From Customer
            accTransactions.setTransactionId(IdGenerator.generateRandom(TRANSACTION_ID_LENGTH));
            accTransactions.setTransactionRefId(IdGenerator.generateRandom(TRANSACTION_ID_LENGTH));
            accTransactions.setAccId(fromAccId);
            accTransactions.setCredit(0.00);
            accTransactions.setDebit(accTransactionsReq.getAmount());
            accTransactions.setAvlBalance(fromAccBalance - accTransactionsReq.getAmount());
            accTransactions.setLastUpdated(Timestamp.from(Instant.now()));
            //accTransactionsRepo.save(accTransactions);
            /*accBalance.setBalance(accTransactions.getAvlBalance());
            accBalanceRepo.save(accBalance);*/

            // To Customer
            accTransactions.setTransactionId(IdGenerator.generateRandom(TRANSACTION_ID_LENGTH));
            accTransactions.setTransactionRefId(accTransactions.getTransactionRefId());
            accTransactions.setAccId(toAccId);
            accTransactions.setCredit(accTransactionsReq.getAmount());
            accTransactions.setDebit(0.00);
            accTransactions.setAvlBalance(fromAccBalance + accTransactionsReq.getAmount());
            accTransactions.setLastUpdated(Timestamp.from(Instant.now()));
            return accTransactionsRepo.save(accTransactions).getTransactionRefId();
        }

        if(accTransactionsReq.getType()=="DEBIT" && toAccBalance>=accTransactionsReq.getAmount()){

            // From Customer
            accTransactions.setTransactionId(IdGenerator.generateRandom(TRANSACTION_ID_LENGTH));
            accTransactions.setTransactionRefId(IdGenerator.generateRandom(TRANSACTION_ID_LENGTH));
            accTransactions.setAccId(fromAccId);
            accTransactions.setCredit(accTransactionsReq.getAmount());
            accTransactions.setDebit(0.00);
            accTransactions.setAvlBalance(fromAccBalance + accTransactionsReq.getAmount());
            accTransactions.setLastUpdated(Timestamp.from(Instant.now()));
            //accTransactionsRepo.save(accTransactions);

            // To Customer
            accTransactions.setTransactionId(IdGenerator.generateRandom(TRANSACTION_ID_LENGTH));
            accTransactions.setTransactionRefId(accTransactions.getTransactionRefId());
            accTransactions.setAccId(toAccId);
            accTransactions.setCredit(0.00);
            accTransactions.setDebit(accTransactionsReq.getAmount());
            accTransactions.setAvlBalance(fromAccBalance - accTransactionsReq.getAmount());
            accTransactions.setLastUpdated(Timestamp.from(Instant.now()));
            return accTransactionsRepo.save(accTransactions).getTransactionRefId();
        }
        return null;
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
        return "details are saved for : " + custCredentials.getCustId();
    }

    @Override
    public Optional<AccBalance> getBalances(Long custId, Long accId) {
        if (custId == 0 && accId != 0) {
            return accBalanceRepo.findById(accId);
        }
        if(custId != 0 && accId == 0){
            Long accountId = custAccMapRepo.findAccIdByCustId(custId);
            return accBalanceRepo.findById(accountId);
        }
        return null;
    }

    @Override
    public Optional<CustDetails> getCustomerDetails(Long custId) {
        return custDetailsRepo.findById(custId);
    }


}
