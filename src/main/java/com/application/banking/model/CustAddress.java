package com.application.banking.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;

@Data
@Entity
public class CustAddress {
    @Id
    private long addressId;
    private String country;
    private String city;
    private String addressLane;
    private long pin;
    private Timestamp lastUpdated;
}
