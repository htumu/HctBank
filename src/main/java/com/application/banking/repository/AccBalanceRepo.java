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
    public Double findBalanceByAccId(Long accId);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update AccBalance set balance = balance+?2 where accId = ?1")
    public void creditBalanceByAccId(Long accId, Double amount);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update AccBalance set balance = balance-?2 where accId = ?1")
    public void debitBalanceByAccId(Long accId, Double amount);

}
