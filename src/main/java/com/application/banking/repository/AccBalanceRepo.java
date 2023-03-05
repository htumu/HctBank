package com.application.banking.repository;

import com.application.banking.model.AccBalance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccBalanceRepo extends JpaRepository<AccBalance, Long> {
}
