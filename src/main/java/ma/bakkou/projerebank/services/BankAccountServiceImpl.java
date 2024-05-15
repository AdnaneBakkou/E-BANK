package ma.bakkou.projerebank.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.bakkou.projerebank.dtos.CustomerDTO;
import ma.bakkou.projerebank.entities.*;
import ma.bakkou.projerebank.enums.OperationType;
import ma.bakkou.projerebank.exceptions.BalanceNotSufficentException;
import ma.bakkou.projerebank.exceptions.BankAccountNotFoundException;
import ma.bakkou.projerebank.exceptions.CustomerNotFoundException;
import ma.bakkou.projerebank.mappers.BankAccountMapperImpl;
import ma.bakkou.projerebank.repositories.AccountOperationRepository;
import ma.bakkou.projerebank.repositories.BankAccountRepository;
import ma.bakkou.projerebank.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@Transactional
@AllArgsConstructor(onConstructor_ = @Autowired) // Lombok will add @Autowired to the generated constructor
@Slf4j
public class BankAccountServiceImpl implements BankAccountService{


    private CustomerRepository customerRepository  ;
    private BankAccountRepository bankAccountRepository;
    private AccountOperationRepository accountOperationRepository;
    private BankAccountMapperImpl dtoMapper;


    @Override
    public CustomerDTO saveCustmer(CustomerDTO customerDTO) {
        log.info("saving new customer");
        Customer customer = dtoMapper.fromCustomerDTO(customerDTO);
       Customer savedCustomer  = customerRepository.save(customer);
        return dtoMapper.fromCustomer(savedCustomer);
    }



    @Override
    public SavingAccount saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if(customer == null){
            throw  new CustomerNotFoundException("customer not found");
        }
        SavingAccount savingAccount = new SavingAccount();
        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setCreatedAt(new Date());
        savingAccount.setBalance(initialBalance);
        savingAccount.setInterestRate(interestRate);
        savingAccount.setCustomer(customer);
        SavingAccount savedBankAccount =  bankAccountRepository.save(savingAccount);
        return savedBankAccount;

    }

    @Override
    public CurrentAccount saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if(customer == null){
            throw  new CustomerNotFoundException("customer not found");
        }
        CurrentAccount currentAccount = new CurrentAccount();
        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setCreatedAt(new Date());
        currentAccount.setBalance(initialBalance);
        currentAccount.setOverDraft(overDraft);
        currentAccount.setCustomer(customer);
       CurrentAccount savedBankAccount =  bankAccountRepository.save(currentAccount);
        return savedBankAccount;

    }

    @Override
    public List<CustomerDTO> listCustomers() {
            List<Customer> customers = customerRepository.findAll();
            List<CustomerDTO> collect =  customers.stream()
                    .map(cust->dtoMapper.fromCustomer(cust))
                    .collect(Collectors.toList());
        return collect;
    }

    @Override
    public BankAccount getBankAccount(String accountId) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(()->new BankAccountNotFoundException("BankAccount Not Found"));
                return bankAccount;
    }

    @Override
    public void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficentException {
        if (amount < 0) {
            throw new IllegalArgumentException("Debit amount must be positive.");
        }

        log.debug("Attempting to debit account: {}, Amount: {}", accountId, amount);
        BankAccount bankAccount = getBankAccount(accountId);

        if (bankAccount.getBalance() < amount) {
            log.error("Insufficient balance for debit operation. Account ID: {}, Required: {}, Available: {}", accountId, amount, bankAccount.getBalance());
            throw new BalanceNotSufficentException("Insufficient balance for debit operation on account " + accountId + ". Attempted debit: " + amount + ", Available balance: " + bankAccount.getBalance());
        }

        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setType(OperationType.DEBIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setOperationDate(new Date());
        accountOperation.setBankAccount(bankAccount);
        accountOperationRepository.save(accountOperation);

        bankAccount.setBalance(bankAccount.getBalance() - amount);
        bankAccountRepository.save(bankAccount);
        log.info("Debit operation successful. Account ID: {}, New Balance: {}", accountId, bankAccount.getBalance());
    }


    @Override
    public void credit(String accountId, double amount, String description) throws BankAccountNotFoundException {
        BankAccount bankAccount = getBankAccount(accountId);
         AccountOperation accountOperation = new AccountOperation();
        accountOperation.setType(OperationType.CREDIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setOperationDate(new Date());
        accountOperation.setBankAccount(bankAccount);
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance()+amount);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void transfer(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFoundException, BalanceNotSufficentException {
        debit(accountIdSource , amount ,"Transfer to "+accountIdDestination);
        credit(accountIdSource , amount ,"Transfer from "+accountIdSource);


    }

@Override
public List<BankAccount> bankAccountsList(){
        return bankAccountRepository.findAll();
    }

    @Override
    public  CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException {
      Customer customer = customerRepository.findById(customerId).orElseThrow(()-> new CustomerNotFoundException("customer not found"));
    return dtoMapper.fromCustomer(customer);
    }


    @Override
    public CustomerDTO updateCustmer(CustomerDTO customerDTO) {
        log.info("saving new customer");
        Customer customer = dtoMapper.fromCustomerDTO(customerDTO);
        Customer savedCustomer  = customerRepository.save(customer);
        return dtoMapper.fromCustomer(savedCustomer);
    }


    public void deleteCustomer(Long customerId){
        customerRepository.deleteById(customerId);
    }







}



