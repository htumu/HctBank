package com.application.banking.model;

import com.application.banking.password.ValidPassword;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Data
@Entity
@Table(name = "cust_credentials")
public class CustCredentials {
    @Id
    @Column(name = "custId")
    private long custId;
    @Column(name = "password")
    private String password;
}
