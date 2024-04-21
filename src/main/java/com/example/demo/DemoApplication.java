package com.example.demo;

import com.example.demo.entities.AccountOperation;
import com.example.demo.entities.CurrentAccount;
import com.example.demo.entities.Customer;
import com.example.demo.entities.SavingAccount;
import com.example.demo.enumeration.AccountStatus;
import com.example.demo.enumeration.OperationType;
import com.example.demo.repositories.AccountOperationRepository;
import com.example.demo.repositories.BankAccountRepository;
import com.example.demo.repositories.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	CommandLineRunner start(CustomerRepository customerRepository,
							BankAccountRepository bankAccountRepository,
							AccountOperationRepository accountOperationRepository){
		return args -> {
			Stream.of("HASSAN","YASSINE","MOUHAMED").forEach(name->{
//				Customer customer=new Customer();
//				customer.setName(name);
//				customer.setEmail(name+"@gmail.com");
				Customer customer = Customer.builder().name(name).email(name+"@gmail.com").build();
				customerRepository.save(customer);

				customerRepository.findAll().forEach(cust->{

					CurrentAccount currentAccount = new CurrentAccount();
					currentAccount.setId(UUID.randomUUID().toString());
					currentAccount.setBalance(Math.random()*90000);
					currentAccount.setCreatedAt(new Date());
					currentAccount.setStatus(AccountStatus.CREATED);
					currentAccount.setCustomer(cust);
					currentAccount.setOverDraft(9000);
					bankAccountRepository.save(currentAccount);


					SavingAccount savingAccount = new SavingAccount();
					savingAccount.setId(UUID.randomUUID().toString());
					savingAccount.setBalance(Math.random()*90000);
					savingAccount.setCreatedAt(new Date());
					savingAccount.setStatus(AccountStatus.CREATED);
					savingAccount.setCustomer(cust);
					savingAccount.setInterestRate(5.5);
					bankAccountRepository.save(savingAccount);


					bankAccountRepository.findAll().forEach(acc->{
						for (int i = 1 ; i<10 ; i++){
							AccountOperation accountOperation = new AccountOperation();
							accountOperation.setOperationDate(new Date());
							accountOperation.setAmount(Math.random()*12000);
							accountOperation.setType(Math.random()>0.5? OperationType.DEBIT:OperationType.CREDIT);
							accountOperation.setBankAccount(acc);
							accountOperationRepository.save(accountOperation);
						}
					});

				});
			});

		};
	}
}
