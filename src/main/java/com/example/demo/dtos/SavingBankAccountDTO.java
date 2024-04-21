package com.example.demo.dtos;

import com.example.demo.entities.AccountOperation;
import com.example.demo.entities.Customer;
import com.example.demo.enumeration.AccountStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SavingBankAccountDTO  extends BankAccountDTO{


    private String id;
    private double balance;

    private AccountStatus status;
    private Date createdAt;

    private CustomerDTO customerDTO;
    private double interestRate;


}
