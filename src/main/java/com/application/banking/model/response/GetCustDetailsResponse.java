package com.application.banking.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
public class GetCustDetailsResponse implements IResponse{
    private String name;
    private Long phone;
    private String email;
    private Long custId;
    private Long accId;
    private Timestamp created;

}
