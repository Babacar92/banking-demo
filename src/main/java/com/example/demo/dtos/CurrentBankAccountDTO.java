package com.example.demo.dtos;

import com.example.demo.entities.Customer;
import com.example.demo.enumeration.AccountStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrentBankAccountDTO extends BankAccountDTO {


    private String id;
    private double balance;

    private AccountStatus status;
    private Date createdAt;

    private CustomerDTO customerDTO;
    private double overDraft;


}
