package com.example.demo.services;

import com.example.demo.dtos.*;
import com.example.demo.entities.*;
import com.example.demo.enumeration.OperationType;
import com.example.demo.exceptions.BalanceNotSufficientException;
import com.example.demo.exceptions.BankAccountNotFoundException;
import com.example.demo.exceptions.CustomerNotFoundException;
import com.example.demo.mappers.BankAccountMapperImpl;
import com.example.demo.repositories.AccountOperationRepository;
import com.example.demo.repositories.BankAccountRepository;
import com.example.demo.repositories.CustomerRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
//@NoArgsConstructor
public class BankAccountServiceImpl implements BankAccountService{

    private CustomerRepository customerRepository;
    private BankAccountRepository bankAccountRepository;
    private AccountOperationRepository accountOperationRepository;
    private BankAccountMapperImpl dtoMapper;

    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        Customer newCustomer =customerRepository.save(dtoMapper.fromCustomerDTO(customerDTO));

        return dtoMapper.fromCustomer(newCustomer);

    }

    @Override
    public CustomerDTO updateCustomer(CustomerDTO customerDTO) {
        Customer newCustomer =customerRepository.save(dtoMapper.fromCustomerDTO(customerDTO));

        return dtoMapper.fromCustomer(newCustomer);

    }

    @Override
    public void deleteCustomer(Long customerId){
        customerRepository.deleteById(customerId);
    }

    @Override
    public CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if (customer == null)
            throw new CustomerNotFoundException("customer not found");
        CurrentAccount currentAccount = new CurrentAccount();
        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setBalance(initialBalance);
        currentAccount.setCreatedAt(new Date());
        currentAccount.setCustomer(customer);
        currentAccount.setOverDraft(overDraft);
         bankAccountRepository.save(currentAccount);

         return  dtoMapper.fromCurrentBankAccount(currentAccount);
    }


    @Override
    public SavingBankAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if (customer == null)
            throw new CustomerNotFoundException("customer not found");
        SavingAccount savingAccount = new SavingAccount();
        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setBalance(initialBalance);
        savingAccount.setCreatedAt(new Date());
        savingAccount.setCustomer(customer);
       savingAccount.setInterestRate(interestRate);
        bankAccountRepository.save(savingAccount);

        return dtoMapper.fromSavingBankAccount(savingAccount);


    }

    @Override
    public List<CustomerDTO> listCustomers() {
       List<Customer> customers = customerRepository.findAll();
      return  customers.stream().map(customer -> dtoMapper.fromCustomer(customer)).collect(Collectors.toList());

    }

    @Override
    public BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException {
   BankAccount bankAccount = bankAccountRepository.findById(accountId).orElse(null);
   if (bankAccount == null)
       throw new BankAccountNotFoundException("Account not found");
   if (bankAccount instanceof SavingAccount){
       SavingAccount savingAccount = (SavingAccount) bankAccount;
       return dtoMapper.fromSavingBankAccount(savingAccount);
   } else {
       CurrentAccount currentAccount = (CurrentAccount) bankAccount;
               return dtoMapper.fromCurrentBankAccount(currentAccount);
   }

    }

    @Override
    public void debit(String accountId, double amount, String description) throws BankAccountNotFoundException , BalanceNotSufficientException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId).orElse(null);
        if (bankAccount == null)
            throw new BankAccountNotFoundException("Account not found");
        if (bankAccount.getBalance() < amount){
            throw new BalanceNotSufficientException("Balance not sufficient");
        }

        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setType(OperationType.DEBIT);
        accountOperation.setAmount(amount);
        accountOperation.setDecription(description);
        accountOperation.setOperationDate(new Date());
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance()-amount);
        bankAccountRepository.save(bankAccount);


    }

    @Override
    public void credit(String accountId, double amount, String description) throws BankAccountNotFoundException{
        BankAccount bankAccount = bankAccountRepository.findById(accountId).orElse(null);
        if (bankAccount == null)
            throw new BankAccountNotFoundException("Account not found");


        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setType(OperationType.DEBIT);
        accountOperation.setAmount(amount);
        accountOperation.setDecription(description);
        accountOperation.setOperationDate(new Date());
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance()+amount);
        bankAccountRepository.save(bankAccount);

    }

    @Override
    public void transfer(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFoundException , BalanceNotSufficientException {
        debit(accountIdSource , amount , "transfer") ;
        credit(accountIdDestination , amount , "transfer");
    }

    @Override
    public List<BankAccountDTO> bankAccountList() {
        List<BankAccount> allAccount = bankAccountRepository.findAll();
       List<BankAccountDTO> bankAccountDTOS = allAccount.stream().map(bankAccount -> {
            if (bankAccount instanceof SavingAccount)

                return dtoMapper.fromSavingBankAccount((SavingAccount) bankAccount);
            else
                return dtoMapper.fromCurrentBankAccount((CurrentAccount) bankAccount);

        }).collect(Collectors.toList());
        return bankAccountDTOS;
    }

    @Override
    public CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException{
        Customer customer= customerRepository.findById(customerId)
                .orElseThrow(()->new CustomerNotFoundException("Customer not found"));

        return dtoMapper.fromCustomer(customer);

    }

    @Override
    public List<AccountOperationDTO> historiqueOperation(String accountId){
        List<AccountOperation> allOperations = accountOperationRepository.findByBankAccountId(accountId);
        return allOperations.stream().map(operation-> dtoMapper.fromAccountOperation(operation)).collect(Collectors.toList());



    }
}
