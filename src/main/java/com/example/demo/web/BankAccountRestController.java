package com.example.demo.web;

import com.example.demo.dtos.AccountOperationDTO;
import com.example.demo.dtos.BankAccountDTO;
import com.example.demo.dtos.CustomerDTO;
import com.example.demo.exceptions.BankAccountNotFoundException;
import com.example.demo.services.BankAccountService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor

public class BankAccountRestController {

    private BankAccountService bankAccountService;

    @GetMapping("/accounts/{accountId}")
    public BankAccountDTO getBankAccount(@PathVariable() String accountId) throws BankAccountNotFoundException {
        return bankAccountService.getBankAccount(accountId);

    }

    @GetMapping("/accounts")
    public List<BankAccountDTO> getBankAccounts(){
        return bankAccountService.bankAccountList();

    }

    @GetMapping("/accounts/{accountId}/operations")
    public List<AccountOperationDTO> history(@PathVariable String accountId){
        return bankAccountService.historiqueOperation(accountId);

    }
}
