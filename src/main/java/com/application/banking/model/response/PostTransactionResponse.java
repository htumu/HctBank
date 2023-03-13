package com.application.banking.model.response;

import lombok.Data;

@Data
public class PostTransactionResponse {
    String message;
    String statusCode;
    Long transactionRefId;

    public PostTransactionResponse(String message, String statusCode, Long transactionRefId) {
        this.message = message;
        this.statusCode = statusCode;
        this.transactionRefId = transactionRefId;
    }
}
