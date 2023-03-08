package com.application.banking.repository;

import com.application.banking.model.AccBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AccBalanceRepo extends JpaRepository<AccBalance, Long> {
    @Query("select balance from AccBalance where accId = ?1")
    public Long findBalanceByAccId(Long accId);
}
