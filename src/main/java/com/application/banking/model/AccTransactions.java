package com.application.banking.model;

import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;

@Data
@Entity
public class AccTransactions {
    @Id
    private long transactionId;
    private long transactionRefId;
    private long accId;
    private double credit;
    private double debit;
    private double avlBalance;
    private Timestamp lastUpdated;
}
