package com.application.banking.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "acc_transactions")
public class AccTransactions {
    @Id
    @Column(name = "transactionId", nullable = false)
    private long transactionId;
    @Column(name = "transactionRefId",nullable = false)
    private long transactionRefId;
    @Column(name = "accId",nullable = false)
    private long accId;
    @Column(name = "credit")
    private double credit;
    @Column(name = "debit")
    private double debit;
    @Column(name = "avlBalance")
    private double avlBalance;
    @Column(name = "lastUpdated", nullable = false)
    private Timestamp lastUpdated;

}
