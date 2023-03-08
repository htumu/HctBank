package com.application.banking.model.response;

import com.application.banking.model.CustDetails;

import javax.persistence.Column;
import java.sql.Timestamp;
import java.util.Optional;

public class GetCustDetailsResponse implements IResponse{
    private Long custId;
    private String name;
    private Long addressId;
    private Long phone;
    private String email;
    private Timestamp created;
    private Timestamp lastUpdated;

    public GetCustDetailsResponse(Long custId, String name, Long addressId, Long phone, String email, Timestamp created, Timestamp lastUpdated) {
        this.custId = custId;
        this.name = name;
        this.addressId = addressId;
        this.phone = phone;
        this.email = email;
        this.created = created;
        this.lastUpdated = lastUpdated;
    }

    public GetCustDetailsResponse(Optional<CustDetails> byId) {
    }
}
