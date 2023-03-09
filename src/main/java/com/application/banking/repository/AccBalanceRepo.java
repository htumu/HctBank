package com.application.banking.repository;

import com.application.banking.model.AccBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface AccBalanceRepo extends JpaRepository<AccBalance, Long> {
    @Query("select balance from AccBalance where accId = ?1")
    public double findBalanceByAccId(Long accId);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update AccBalance set balance = balance+?2 where accId = ?1")
    public void creditBalanceByAccId(long accId, double balance);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update AccBalance set balance = balance-?2 where accId = ?1")
    public void debitBalanceByAccId(long accId, double balance);

}
