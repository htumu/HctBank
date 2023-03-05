package com.application.banking.model.request;

import lombok.Data;

@Data
public class CustDetailsReq {
    private String name;
    private long phone;
    private CustAddressReq address;
    private String email;
}
