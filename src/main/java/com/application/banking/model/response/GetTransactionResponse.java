package com.application.banking.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;
@Data
@AllArgsConstructor
public class GetTransactionResponse implements IResponse{
    private String name;
    private Long accId;
    private Long transactionRefId;
    private Double credit;
    private Double debit;
    private Double avlBalance;
    private Timestamp transactionDate;
}
