package com.application.banking.service;

import com.application.banking.generateId.IdGenerator;
import com.application.banking.model.*;
import com.application.banking.model.request.*;
import com.application.banking.model.response.CreateResponse;
import com.application.banking.model.response.Response;
import com.application.banking.repository.*;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;

@Service
public class ServiceImpl implements BankService {
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
        Long accIdA = updateAccount(accTransactionsReq.getAccount());
        Long accIdB = updateAccount(accTransactionsReq.getAccount());
        AccTransactions accTransactions = new AccTransactions();
        AccBalance accBalanceA = new AccBalance();
        AccBalance accBalanceB = new AccBalance();


        accTransactions.setAccId(accIdA);
        accTransactions.setAccId(accIdB);
        accTransactions.setCredit(accTransactionsReq.getCredit());
        accTransactions.setDebit(accTransactionsReq.getDebit());

        if (accTransactions.getDebit() == 0 && accTransactions.getCredit() != 0 && accBalanceB.getBalance() >= accTransactions.getCredit()) {
            accBalanceA.setBalance(accBalanceA.getBalance() + accTransactions.getCredit());
            accTransactions.setTransactionId(IdGenerator.generateRandom(TRANSACTION_ID_LENGTH));

            accBalanceB.setBalance(accBalanceB.getBalance() - accTransactions.getCredit());
            accTransactions.setTransactionId(IdGenerator.generateRandom(TRANSACTION_ID_LENGTH));

            accTransactions.setTransactionRefId(IdGenerator.generateRandom(TRANSACTION_ID_LENGTH));
        }
        if (accTransactions.getDebit() != 0 && accTransactions.getCredit() == 0 && accBalanceA.getBalance() >= accTransactions.getDebit()) {

            accBalanceA.setBalance(accBalanceA.getBalance() - accTransactions.getDebit());
            accTransactions.setTransactionId(IdGenerator.generateRandom(TRANSACTION_ID_LENGTH));

            accBalanceB.setBalance(accBalanceB.getBalance() + accTransactions.getDebit());
            accTransactions.setTransactionId(IdGenerator.generateRandom(TRANSACTION_ID_LENGTH));

            accTransactions.setTransactionRefId(IdGenerator.generateRandom(TRANSACTION_ID_LENGTH));

        }

        accBalanceRepo.save(accBalanceA);
        accBalanceRepo.save(accBalanceB);
        accTransactions.setAvlBalance(accBalanceA.getBalance());
        accTransactions.setAvlBalance(accBalanceB.getBalance());
        accTransactions.setLastUpdated(Timestamp.from(Instant.now()));
        return accTransactionsRepo.save(accTransactions).getTransactionRefId();
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

    public Long creditAmount(AccTransactionsReq accTransactionsReq) {
        Long accId = updateAccount(accTransactionsReq.getAccount());
        AccTransactions accTransactions = new AccTransactions();
        AccBalance accBalance = new AccBalance();

        accTransactions.setAccId(accId);
        accTransactions.setCredit(accTransactionsReq.getCredit());
        accTransactions.setDebit(0);

        accBalance.setBalance(accBalance.getBalance() + accTransactions.getCredit());
        accTransactions.setTransactionId(IdGenerator.generateRandom(TRANSACTION_ID_LENGTH));
        accTransactions.setTransactionRefId(IdGenerator.generateRandom(TRANSACTION_ID_LENGTH));
        accBalanceRepo.save(accBalance);
        accTransactions.setAvlBalance(accBalance.getBalance());
        accTransactions.setLastUpdated(Timestamp.from(Instant.now()));
        return accTransactionsRepo.save(accTransactions).getTransactionId();
    }

    public Long debitAmount(AccTransactionsReq accTransactionsReq) {
        Long accId = updateAccount(accTransactionsReq.getAccount());
        AccTransactions accTransactions = new AccTransactions();
        AccBalance accBalance = new AccBalance();

        accTransactions.setAccId(accId);
        accTransactions.setDebit(accTransactionsReq.getDebit());
        accTransactions.setCredit(0);
        if (accBalance.getBalance() >= accTransactions.getDebit()) {
            accBalance.setBalance(accBalance.getBalance() - accTransactions.getDebit());
            accTransactions.setTransactionId(IdGenerator.generateRandom(TRANSACTION_ID_LENGTH));
            accTransactions.setTransactionRefId(IdGenerator.generateRandom(TRANSACTION_ID_LENGTH));
        }
        accBalanceRepo.save(accBalance);
        accTransactions.setAvlBalance(accBalance.getBalance());
        accTransactions.setLastUpdated(Timestamp.from(Instant.now()));
        return accTransactionsRepo.save(accTransactions).getTransactionId();
    }
}
