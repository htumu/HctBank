package com.application.banking.model;

import lombok.Data;
import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
public class CustDetails {
    @Id
    private long custId;
    private String name;
    private long addressId;
    private long phone;
    private String email;
    private Timestamp created;
    private Timestamp lastUpdated;

}
