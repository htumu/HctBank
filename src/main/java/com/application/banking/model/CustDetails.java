package com.application.banking.model;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.sql.Timestamp;

@Data
@Entity
public class CustDetails {
    @Id
    private long custId;
    @NotEmpty(message = "Name is Required!")
    private String name;
    private long addressId;
    @NotNull(message = "Phone number is Required! ")
    private long phone;
    @NotEmpty(message = "Email is Required!")
    @Email
    private String email;
    private Timestamp created;
    private Timestamp lastUpdated;

}
