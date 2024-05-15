package ma.bakkou.projerebank.services;

import ma.bakkou.projerebank.dtos.CustomerDTO;
import ma.bakkou.projerebank.entities.BankAccount;
import ma.bakkou.projerebank.entities.CurrentAccount;
import ma.bakkou.projerebank.entities.SavingAccount;
import ma.bakkou.projerebank.exceptions.BalanceNotSufficentException;
import ma.bakkou.projerebank.exceptions.BankAccountNotFoundException;
import ma.bakkou.projerebank.exceptions.CustomerNotFoundException;

import java.util.List;

public interface BankAccountService {
    public CustomerDTO saveCustmer(CustomerDTO customerDTO);
    SavingAccount saveSavingBankAccount(double initialBalance, double interestRate , Long customerId ) throws CustomerNotFoundException;

    CurrentAccount saveCurrentBankAccount(double initialBalance, double overDraft , Long customerId ) throws CustomerNotFoundException;
    List<CustomerDTO> listCustomers();
    BankAccount getBankAccount(String accountId) throws BankAccountNotFoundException;
    void debit(String accountId , double amount , String description) throws BankAccountNotFoundException, BalanceNotSufficentException;
    void credit(String accountId , double amount , String description) throws BankAccountNotFoundException, BalanceNotSufficentException;
    void transfer(String accountIdSource , String accountIdDestination,double amount) throws BankAccountNotFoundException, BalanceNotSufficentException;

    List<BankAccount> bankAccountsList();

    CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException;

    CustomerDTO updateCustmer(CustomerDTO customerDTO);
}
