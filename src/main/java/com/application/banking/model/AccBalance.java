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
    @Column(name = "accId")
    private long accId;
    @Column(name = "balance")
    private double balance;
}
