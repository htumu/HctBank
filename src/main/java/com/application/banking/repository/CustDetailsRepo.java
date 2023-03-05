package com.application.banking.repository;

import com.application.banking.model.CustDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustDetailsRepo extends JpaRepository<CustDetails, Long> {
}
