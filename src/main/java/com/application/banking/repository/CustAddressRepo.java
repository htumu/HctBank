package com.application.banking.repository;

import com.application.banking.model.CustAddress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustAddressRepo extends JpaRepository<CustAddress,Long> {
}
