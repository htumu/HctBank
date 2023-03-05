package com.application.banking.model.request;

import lombok.Data;

import javax.validation.constraints.PositiveOrZero;

@Data
public class AccTransactionsReq {
    private AccBalanceReq account;
    @PositiveOrZero
    private double credit;
    @PositiveOrZero
    private double debit;
}
