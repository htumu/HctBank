package com.application.banking.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GetTransactionResponses implements IResponse{
    List<GetTransactionResponse> transactionDetails;
}
