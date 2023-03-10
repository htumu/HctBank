package com.application.banking.model.request;

import lombok.Data;

import javax.validation.constraints.PositiveOrZero;

@Data
public class AccBalanceReq {
    private long accId;
    @PositiveOrZero
    private double balance;
}
