package com.application.banking.repository;

import com.application.banking.model.AccTransactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AccTransactionsRepo extends JpaRepository<AccTransactions, Long> {
    @Query("select transactionId from AccTransactions where accId = ?1 AND transactionRefId = ?2")
    public Long findTIdFromAIdAndTRId(Long accId, Long transactionRefId);

    @Query("select transactionId,transactionRefId,accId,credit,debit,avlBalance,lastUpdated from AccTransactions where accId = ?1")
    public List<Object[]> findTransactionsFromAId(Long accId);

    @Query("select transactionId,transactionRefId,accId,credit,debit,avlBalance,lastUpdated from AccTransactions where transactionRefId = ?1")
    public List<Object[]> findTransactionsFromTRId(Long transactionRefId);
}
