package com.application.banking.service;

import com.application.banking.exception.EntityNotFoundException;
import com.application.banking.exception.NoRecordFoundException;
import com.application.banking.model.response.*;
import com.application.banking.util.IdGenerator;
import com.application.banking.model.*;
import com.application.banking.model.request.*;
import com.application.banking.repository.*;
import com.application.banking.util.HCStatusCode;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
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
    public CreateCustResponse saveCustomerDetails(CustDetailsReq custDetailsReq) {
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
        Double balance = 500.00;

        boolean mapResult = mapAccIdToCustId(new CustAccMap(accId, custId));
        return mapResult ? new CreateCustResponse(custDetailsReq.getName(), custId, accId, balance) : null;
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
    public Object saveTransactions(AccTransactionsReq accTransactionsReq) {
        Long fromAccId = accTransactionsReq.getAccId();
        Long toAccId = accTransactionsReq.getToAccId();
        Double fromAccBalance = accBalanceRepo.findBalanceByAccId(fromAccId);
        Double toAccBalance = accBalanceRepo.findBalanceByAccId(toAccId);
        AccTransactions fromTransaction = new AccTransactions();
        AccTransactions toTransaction = new AccTransactions();

        if (accBalanceRepo.findById(fromAccId).isPresent() && accBalanceRepo.findById(toAccId).isPresent()) {
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
                    return new PostTransactionResponse("Transaction Successful", HCStatusCode.HCTB200, fromTransaction.getTransactionRefId());
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
                    return new PostTransactionResponse("Transaction Successful", HCStatusCode.HCTB200, fromTransaction.getTransactionRefId());
                } else {
                    return "Please provide a valid Debit Amount!";
                }

            } else {
                return "Please give a valid 'type' i.e either CREDIT/DEBIT";
            }
        } else {
            throw new EntityNotFoundException("AccountId Entity is invalid");
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
        return "Password credentials are saved for : " + custCredentials.getCustId();

    }

    public String updatePassword(CustCredentialsReq custCredentialsReq) {
        CustCredentials custCredentials = new CustCredentials();

        custCredentials.setCustId(custCredentialsReq.getCustId());
        custCredentials.setPassword(custCredentialsReq.getPassword());

        custCredentialsRepo.save(custCredentials);
        return "Password credentials are updated for : " + custCredentials.getCustId();

    }

    @Override
    public Optional<AccBalance> getBalances(Long custId, Long accId) {
        if (custDetailsRepo.findById(custId).isPresent() || accBalanceRepo.findById(accId).isPresent()) {
            if (custId == null && accId != 0) {
                return accBalanceRepo.findById(accId);
            }
            if (custId != 0 && accId == null) {
                Long accountId = custAccMapRepo.findAccIdByCustId(custId);
                return accBalanceRepo.findById(accountId);
            }

            return null;
        } else {
            throw new EntityNotFoundException("Given details are invalid!");
        }
    }

    @Override
    public GetCustResponses getCustomerDetails(Long custId) {
        List<GetCustDetailsResponse> custResponseList = new ArrayList<>();
        if (custId == null) {
            List<CustDetails> custDetailsList = custDetailsRepo.findAll();
            for (CustDetails custDetails : custDetailsList) {
                custResponseList.add(new GetCustDetailsResponse(custDetails.getName(), custDetails.getPhone(), custDetails.getEmail(), custDetails.getCustId(), custAccMapRepo.findAccIdByCustId(custDetails.getCustId()), custDetails.getCreated()));
            }

        } else {
            Optional<CustDetails> custDetails = custDetailsRepo.findById(custId);
            if (custDetails.isPresent()) {
                CustDetails customer = custDetails.get();
                custResponseList.add(new GetCustDetailsResponse(customer.getName(), customer.getPhone(), customer.getEmail(), customer.getCustId(), custAccMapRepo.findAccIdByCustId(customer.getCustId()), customer.getCreated()));
            } else {
                throw new EntityNotFoundException("Given custId " + custId + " is invalid!");
            }
        }
        return new GetCustResponses(custResponseList);
    }

    @Override
    public GetTransactionResponses getTransactionResponses(Long accId, Long transactionRefId) {
        List<GetTransactionResponse> transactionResponseList = new ArrayList<>();
        if (accId == null && transactionRefId == null) {
            throw new NoRecordFoundException("Transaction ReferenceId and AccountId cannot be null. Please give valid details.");
        }
        if (accId != null && transactionRefId != null) {
            Optional<AccTransactions> accTransactions = accTransactionsRepo.findById(accTransactionsRepo.findTIdFromAIdAndTRId(accId, transactionRefId));
            if (accTransactions.isPresent()) {
                AccTransactions transactions = accTransactions.get();
                transactionResponseList.add(new GetTransactionResponse(custDetailsRepo.findNameFromCustId(custAccMapRepo.findCustIdByAccId(accId)), accId, transactionRefId, transactions.getCredit(), transactions.getDebit(), transactions.getAvlBalance(), transactions.getLastUpdated()));
            } else {
                throw new EntityNotFoundException("Given Details are invalid!");
            }
        }
        /*if (accId == null && transactionRefId != null) {
            if (accTransactionsRepo.findById(accTransactionsRepo.findTransactionIdFromTRId(transactionRefId)).isPresent()) {
                List<Object[]> accTransactionsList = accTransactionsRepo.findTransactionsFromTRId(transactionRefId);
                List<AccTransactions> accTransList = accTransactionsList.get(0);
                for (AccTransactions accTransactions : accTransList) {
                    transactionResponseList.add(new GetTransactionResponse(custDetailsRepo.findNameFromCustId(custAccMapRepo.findCustIdByAccId(accTransactions.getAccId())), accTransactions.getAccId(), transactionRefId, accTransactions.getCredit(), accTransactions.getDebit(), accTransactions.getAvlBalance(), accTransactions.getLastUpdated()));
                }
            } else {
                throw new EntityNotFoundException("Transaction ReferenceId " + transactionRefId + " is invalid.");
            }
        }
        if (accId != null && transactionRefId == null) {
            if (accBalanceRepo.findById(accId).isPresent()) {
                List<AccTransactions> accTransactionsList = accTransactionsRepo.findTransactionsFromAId(accId);
                for (AccTransactions accTransactions : accTransactionsList) {
                    transactionResponseList.add(new GetTransactionResponse(custDetailsRepo.findNameFromCustId(custAccMapRepo.findCustIdByAccId(accId)), accId, accTransactions.getTransactionRefId(), accTransactions.getCredit(), accTransactions.getDebit(), accTransactions.getAvlBalance(), accTransactions.getLastUpdated()));
                }
            } else {
                throw new EntityNotFoundException("Given AccountId " + accId + " is invalid!");
            }
        }*/
        return new GetTransactionResponses(transactionResponseList);
    }

}