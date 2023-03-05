package com.application.banking.model;

import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class AccBalance {
    @Id
    private long accId;
    private double balance;
}
