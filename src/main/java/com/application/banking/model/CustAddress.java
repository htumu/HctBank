package com.application.banking.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "cust_address")
public class CustAddress {
    @Id
    @Column(name = "addressId")
    private long addressId;
    @Column(name = "country")
    private String country;
    @Column(name = "city")
    private String city;
    @Column(name = "addressLane")
    private String addressLane;
    @Column(name = "pin")
    private long pin;
    @Column(name = "lastUpdated")
    private Timestamp lastUpdated;
}
