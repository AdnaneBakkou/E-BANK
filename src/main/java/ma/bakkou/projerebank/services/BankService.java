package ma.bakkou.projerebank.services;


import jakarta.transaction.Transactional;
import ma.bakkou.projerebank.entities.BankAccount;
import ma.bakkou.projerebank.entities.CurrentAccount;
import ma.bakkou.projerebank.entities.SavingAccount;
import ma.bakkou.projerebank.repositories.BankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class BankService {
    @Autowired
    private BankAccountRepository bankAccountRepository;
    public void consulter(){
        BankAccount bankAccount =
                bankAccountRepository.findById("191fca79-0baa-4f2d-a05a-d76f7322e799").orElse(null);
        if (bankAccount!=null) {

            System.out.println("***************************");
            System.out.println(bankAccount.getId());
            System.out.println(bankAccount.getBalance());
            System.out.println(bankAccount.getStatus());
            System.out.println(bankAccount.getCreatedAt());
            System.out.println(bankAccount.getCustomer().getName());
            System.out.println(bankAccount.getClass().getSimpleName());
            if (bankAccount instanceof CurrentAccount) {
                System.out.println("overdraft" + ((CurrentAccount) bankAccount).getOverDraft());
            } else if (bankAccount instanceof SavingAccount) {
                System.out.println("Rate" + ((SavingAccount) bankAccount).getInterestRate());
            }
            bankAccount.getAccountOperations().forEach(op -> {
                System.out.println("*******");
                System.out.println(op.getType());
                System.out.println(op.getAmount());
                System.out.println(op.getOperationDate());
            });
        }
    }
}
