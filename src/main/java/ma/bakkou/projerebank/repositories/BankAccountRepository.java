package ma.bakkou.projerebank.repositories;

import ma.bakkou.projerebank.entities.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountRepository extends JpaRepository<BankAccount,String> {
}
