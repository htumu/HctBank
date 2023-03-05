package com.application.banking.repository;

import com.application.banking.model.AccTransactions;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccTransactionsRepo extends JpaRepository<AccTransactions, Long> {
}
