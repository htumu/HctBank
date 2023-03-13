package com.application.banking.repository;

import com.application.banking.model.AccTransactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface AccTransactionsRepo extends JpaRepository<AccTransactions, Long> {

    @Query("select transactionId from AccTransactions where transactionRefId = ?1")
    public Long findTransactionIdFromTRId(Long transactionRefId);
    @Query("select transactionId from AccTransactions where accId = ?1 AND transactionRefId = ?2")
    public Long findTIdFromAIdAndTRId(Long accId, Long transactionRefId);

    @Query("select transactionId,transactionRefId,accId,credit,debit,avlBalance,LastUpdated from AccTransactions where accId = ?1")
    public List<Object[]> findTransactionsFromAId(Long accId);

    @Query("select transactionId,transactionRefId,accId,credit,debit,avlBalance,LastUpdated from AccTransactions where transactionRefId = ?1")
    public List<Object[]> findTransactionsFromTRId(Long transactionRefId);
}
