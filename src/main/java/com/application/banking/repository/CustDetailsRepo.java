package com.application.banking.repository;

import com.application.banking.model.CustDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CustDetailsRepo extends JpaRepository<CustDetails, Long> {
    @Query("select name from CustDetails where custId = ?1")
    public String findNameFromCustId(Long custId);
}
