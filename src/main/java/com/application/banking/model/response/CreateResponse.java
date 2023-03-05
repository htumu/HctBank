package com.application.banking.model.response;

import lombok.Data;

@Data
public class CreateResponse implements Response{
    private Long custId;

    public CreateResponse(Long custId) {
        this.custId = custId;
    }
}
