package com.application.banking.service;

import com.application.banking.generateId.IdGenerator;
import com.application.banking.model.*;
import com.application.banking.model.request.*;
import com.application.banking.model.response.CreateCustResponse;
import com.application.banking.model.response.IResponse;
import com.application.banking.repository.*;
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
    public String saveTransactions(AccTransactionsReq accTransactionsReq) {
        Long fromAccId = accTransactionsReq.getFromAccId();
        Long toAccId = accTransactionsReq.getToAccId();
        double fromAccBalance = accBalanceRepo.findBalanceByAccId(fromAccId);
        double toAccBalance = accBalanceRepo.findBalanceByAccId(toAccId);
        AccTransactions fromTransaction = new AccTransactions();
        AccTransactions toTransaction = new AccTransactions();

        if (accTransactionsReq.getType().equals("CREDIT")) {
            if (fromAccBalance >= accTransactionsReq.getAmount()) {
                // From Customer
                fromTransaction.setTransactionId(IdGenerator.generateRandom(TRANSACTION_ID_LENGTH));
                fromTransaction.setTransactionRefId(IdGenerator.generateRandom(TRANSACTION_ID_LENGTH));
                fromTransaction.setAccId(fromAccId);
                fromTransaction.setCredit(0.00);
                fromTransaction.setDebit(accTransactionsReq.getAmount());
                fromTransaction.setAvlBalance(fromAccBalance - accTransactionsReq.getAmount());
                fromTransaction.setLastUpdated(Timestamp.from(Instant.now()));

                accBalanceRepo.debitBalanceByAccId(fromAccId, fromTransaction.getDebit());
                accTransactionsRepo.save(fromTransaction);

                // To Customer
                toTransaction.setTransactionId(IdGenerator.generateRandom(TRANSACTION_ID_LENGTH));
                toTransaction.setTransactionRefId(fromTransaction.getTransactionRefId());
                toTransaction.setAccId(toAccId);
                toTransaction.setCredit(accTransactionsReq.getAmount());
                toTransaction.setDebit(0.00);
                toTransaction.setAvlBalance(toAccBalance + accTransactionsReq.getAmount());
                toTransaction.setLastUpdated(Timestamp.from(Instant.now()));

                accBalanceRepo.creditBalanceByAccId(toAccId, toTransaction.getCredit());
                accTransactionsRepo.save(toTransaction);
                return "The reference Id for the above credit transaction is " + fromTransaction.getTransactionRefId();
            } else {
                return "Please provide a valid Credit Amount!";
            }

        }

        if (accTransactionsReq.getType().equals("DEBIT")) {
            if (toAccBalance >= accTransactionsReq.getAmount()) {
                // From Customer
                fromTransaction.setTransactionId(IdGenerator.generateRandom(TRANSACTION_ID_LENGTH));
                fromTransaction.setTransactionRefId(IdGenerator.generateRandom(TRANSACTION_ID_LENGTH));
                fromTransaction.setAccId(fromAccId);
                fromTransaction.setCredit(accTransactionsReq.getAmount());
                fromTransaction.setDebit(0.00);
                fromTransaction.setAvlBalance(fromAccBalance + accTransactionsReq.getAmount());
                fromTransaction.setLastUpdated(Timestamp.from(Instant.now()));

                accBalanceRepo.creditBalanceByAccId(fromAccId, fromTransaction.getCredit());
                accTransactionsRepo.save(fromTransaction);

                // To Customer
                toTransaction.setTransactionId(IdGenerator.generateRandom(TRANSACTION_ID_LENGTH));
                toTransaction.setTransactionRefId(fromTransaction.getTransactionRefId());
                toTransaction.setAccId(toAccId);
                toTransaction.setCredit(0.00);
                toTransaction.setDebit(accTransactionsReq.getAmount());
                toTransaction.setAvlBalance(toAccBalance - accTransactionsReq.getAmount());
                toTransaction.setLastUpdated(Timestamp.from(Instant.now()));

                accBalanceRepo.debitBalanceByAccId(toAccId, toTransaction.getDebit());
                accTransactionsRepo.save(toTransaction);
                return "The reference Id for the above debit transaction is " + fromTransaction.getTransactionRefId();
            } else {
                return "Please provide a valid Debit Amount!";
            }

        } else {
            return "Please give a valid 'type' i.e either CREDIT/DEBIT";
        }
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
    public Object getBalances(Long custId, Long accId) {
        if (custId == 0 && accId != 0) {
            return accBalanceRepo.findById(accId);
        }
        if (custId != 0 && accId == 0) {
            Long accountId = custAccMapRepo.findAccIdByCustId(custId);
            return accBalanceRepo.findById(accountId);
        }
        if (custId == 0 && accId == 0) {
            return "Both CustomerId and AccountId cannot be zero. Please give valid details.";
        }
        if (custId != 0 && accId != 0) {
            // check if given accountId is associated with the customer.
            Long accountId = custAccMapRepo.findAccIdByCustId(custId);
            if (accountId == accId) {
                return accBalanceRepo.findById(accId);
            } else {
                return "This AccountId is not associated with the Customer!";
            }
        }
        return null;
    }

    @Override
    public Object getCustomerDetails(Long custId) {
        if (custId != 0) {
            return custDetailsRepo.findById(custId);
        } else {
            return custDetailsRepo.findAll();
        }
    }

    @Override
    public Object getTransactions(Long accId, Long transactionRefId) {
        if (transactionRefId == 0 && accId != 0) {
            return accTransactionsRepo.findTransactionsFromAId(accId);
        }
        if (transactionRefId != 0 && accId == 0) {
            return accTransactionsRepo.findTransactionsFromTRId(transactionRefId);
        }
        if (transactionRefId != 0 && accId != 0) {
            return accTransactionsRepo.findById(accTransactionsRepo.findTIdFromAIdAndTRId(accId, transactionRefId));
        }
        if (transactionRefId == 0 && accId == 0) {
            return "Both Transaction ReferenceId and AccountId cannot be zero. Please give valid details.";
        }
        return null;

    }

}