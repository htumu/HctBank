package com.application.banking.model.request;

import lombok.Data;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Data
public class AccTransactionsReq {
    private long fromAccId;
    private long toAccId;
    private String type;
    @Positive
    private double amount;
}
