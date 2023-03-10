package com.application.banking.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "acc_balance")
public class AccBalance {
    @Id
    @Column(name = "accId", nullable = false)
    private long accId;
    @Column(name = "balance", nullable = false)
    private double balance;
}
