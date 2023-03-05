package com.application.banking.model;

import com.application.banking.password.ValidPassword;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Data
@Entity
public class CustCredentials {
    @Id
    private long custId;
    @NotEmpty(message = "Password is required!")
    @ValidPassword
    private String password;
}
