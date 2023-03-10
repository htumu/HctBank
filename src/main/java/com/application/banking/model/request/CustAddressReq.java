package com.application.banking.model.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class CustAddressReq {
    @NotEmpty(message = "Country name cannot be empty!")
    private String country;
    @NotEmpty(message = "City name cannot be empty!")
    private String city;
    @NotEmpty(message = "Lane cannot be empty!")
    private String addressLane;
    @NotEmpty(message = "Pin code cannot be empty!")
    private long pin;
}
