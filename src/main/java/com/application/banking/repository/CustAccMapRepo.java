package com.application.banking.repository;

import com.application.banking.model.CustAccMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CustAccMapRepo extends JpaRepository<CustAccMap,Long> {
    @Query("select accId from CustAccMap where custId = ?1")
    public Long findAccIdByCustId(Long custId);

    @Query("select custId from CustAccMap where accId = ?1")
    public Long findCustIdByAccId(Long accId);
}
