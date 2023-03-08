package com.application.banking.model.response;

import lombok.Data;

@Data

public class CreateCustResponse implements IResponse {
    private Long custId;

    public CreateCustResponse(Long custId) {
        this.custId = custId;
    }
}
