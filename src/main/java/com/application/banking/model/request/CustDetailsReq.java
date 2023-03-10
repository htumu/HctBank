package com.application.banking.model.request;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class CustDetailsReq {
    @NotEmpty(message = "Name is Required!")
    private String name;
    @NotNull(message = "Phone number is Required! ")
    private long phone;
    private CustAddressReq address;
    @NotEmpty(message = "Email is Required!")
    @Email
    private String email;
}
