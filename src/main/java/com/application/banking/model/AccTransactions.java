package com.application.banking.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "acc_transactions")
public class AccTransactions {
    @Id
    @Column(name = "transactionId")
    private long transactionId;
    @Column(name = "transactionRefId")
    private long transactionRefId;
    @Column(name = "accId")
    private long accId;
    @Column(name = "credit")
    private double credit;
    @Column(name = "debit")
    private double debit;
    @Column(name = "avlBalance")
    private double avlBalance;
    @Column(name = "lastUpdated")
    private Timestamp lastUpdated;
}
