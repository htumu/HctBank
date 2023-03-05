package com.application.banking.model.request;

import lombok.Data;

@Data
public class AccBalanceReq {
    private long accId;
    private double balance;
}
