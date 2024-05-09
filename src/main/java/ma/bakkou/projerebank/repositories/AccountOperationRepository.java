package ma.bakkou.projerebank.repositories;

import ma.bakkou.projerebank.entities.AccountOperation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountOperationRepository extends JpaRepository<AccountOperation,Long> {
}
