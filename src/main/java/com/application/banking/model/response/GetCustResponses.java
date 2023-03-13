package com.application.banking.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
@Data
@AllArgsConstructor
public class GetCustResponses implements IResponse{
    List<GetCustDetailsResponse> customerDetails;
}
