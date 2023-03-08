package com.application.banking.model.request;

import com.application.banking.password.ValidPassword;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class CustCredentialsReq {
    private long custId;
    @NotEmpty(message = "Password is required!")
    private String password;
}
