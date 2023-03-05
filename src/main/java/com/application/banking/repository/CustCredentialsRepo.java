package com.application.banking.repository;

import com.application.banking.model.CustCredentials;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustCredentialsRepo extends JpaRepository<CustCredentials, Long> {
}
