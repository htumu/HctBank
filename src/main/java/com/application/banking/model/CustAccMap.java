package com.application.banking.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cust_acc_map")
public class CustAccMap {
    @Id
    @Column(name = "accId")
    private long accId;
    @Column(name = "custId")
    private long custId;
}
