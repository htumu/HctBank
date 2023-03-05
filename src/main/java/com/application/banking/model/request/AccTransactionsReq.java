package com.application.banking.model.request;

import lombok.Data;

@Data
public class AccTransactionsReq {
    private double credit;
    private double debit;
}
