package com.application.banking.model.request;

import lombok.Data;

@Data
public class CustCredentialsReq {
    private long custId;
    private String password;
}
