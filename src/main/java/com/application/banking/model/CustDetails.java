package com.application.banking.model;

import lombok.Data;
import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "cust_details")
public class CustDetails {
    @Id
    @Column(name = "custId")
    private long custId;
    @Column(name = "name")
    private String name;
    @Column(name = "addressId")
    private long addressId;
    @Column(name = "phone")
    private long phone;
    @Column(name = "email")
    private String email;
    @Column(name = "created")
    private Timestamp created;
    @Column(name = "lastUpdated")
    private Timestamp lastUpdated;

}
