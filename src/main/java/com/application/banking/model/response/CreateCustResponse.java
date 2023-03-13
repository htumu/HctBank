package com.application.banking.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateCustResponse implements IResponse {
    private String name;
    private Long custId;
    private Long accId;
    private Double balance;
}
