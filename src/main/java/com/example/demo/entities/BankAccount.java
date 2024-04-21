package com.example.demo.entities;

import com.example.demo.enumeration.AccountStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TYPE",discriminatorType = DiscriminatorType.STRING)
public class BankAccount {
    @Id
    private String id;
    private double balance;
    @Enumerated(EnumType.STRING)
    private AccountStatus status;
    private Date createdAt;
    @ManyToOne
    private Customer customer;

    @OneToMany(mappedBy = "bankAccount" ,fetch = FetchType.LAZY)
    private List<AccountOperation> accountOperations;

}
