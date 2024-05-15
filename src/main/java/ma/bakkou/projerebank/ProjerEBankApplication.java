package ma.bakkou.projerebank;

import ma.bakkou.projerebank.dtos.CustomerDTO;
import ma.bakkou.projerebank.entities.*;
import ma.bakkou.projerebank.enums.AccountStatus;
import ma.bakkou.projerebank.enums.OperationType;
import ma.bakkou.projerebank.exceptions.BalanceNotSufficentException;
import ma.bakkou.projerebank.exceptions.BankAccountNotFoundException;
import ma.bakkou.projerebank.exceptions.CustomerNotFoundException;
import ma.bakkou.projerebank.repositories.AccountOperationRepository;
import ma.bakkou.projerebank.repositories.BankAccountRepository;
import ma.bakkou.projerebank.repositories.CustomerRepository;
import ma.bakkou.projerebank.services.BankAccountService;
import ma.bakkou.projerebank.services.BankService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class ProjerEBankApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjerEBankApplication.class, args);
    }



    @Bean
    CommandLineRunner commandLineRunner(BankAccountService bankAccountService){
        return args -> {

            Stream.of("hassan","imane","mohamed").forEach(name->{
                CustomerDTO customer = new CustomerDTO();
                customer.setName(name);
                customer.setEmail(name+"gmail.com");
                bankAccountService.saveCustmer(customer);
            });
            bankAccountService.listCustomers().forEach(customer -> {
                try {
                    bankAccountService.saveCurrentBankAccount(Math.random()*10000,9000, customer.getId());
                    bankAccountService.saveSavingBankAccount(Math.random()*10000,5.5,customer.getId());
                    List<BankAccount> bankAccounts = bankAccountService.bankAccountsList();
                    for (BankAccount bankAccount:bankAccounts){
                        for (int i = 0 ; i<5;i++){
                            bankAccountService.credit(bankAccount.getId(),9000+Math.random()*1000,"crédit");

                        }
                        for (int i = 0 ; i<5;i++){
                            bankAccountService.debit(bankAccount.getId(),1000+Math.random()*9000,"debit");

                        }
                    }
                } catch (CustomerNotFoundException e) {
                    throw new RuntimeException(e);
                }
                catch (BankAccountNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (BalanceNotSufficentException e) {
                    throw new RuntimeException(e);
                }


            });
        };
    }



    // @Bean
    CommandLineRunner start(CustomerRepository customerRepository ,
                            BankAccountRepository bankAccountRepository,
                            AccountOperationRepository accountOperationRepository){

        return args->{


            Stream.of("hassan","yassine","aicha").forEach(name->{
                Customer customer = new Customer();
                customer.setName(name);
                customer.setEmail(name+"@gmail.com");
                customerRepository.save(customer);
            });
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
            });


            bankAccountRepository.findAll().forEach(acc->{
                for (int i = 0 ; i<10 ; i++){
                    AccountOperation accountOperation = new AccountOperation();
                    accountOperation.setOperationDate(new Date());
                    accountOperation.setAmount(Math.random()*12000);
                    accountOperation.setType(Math.random()>0.5? OperationType.DEBIT : OperationType.CREDIT);
                    accountOperation.setBankAccount(acc);
                    accountOperationRepository.save(accountOperation);
                }


            });


        };

    }


}
