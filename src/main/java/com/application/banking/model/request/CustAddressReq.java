package com.application.banking.model.request;

import lombok.Data;

@Data
public class CustAddressReq {
    private String country;
    private String city;
    private String addressLane;
    private long pin;
}
