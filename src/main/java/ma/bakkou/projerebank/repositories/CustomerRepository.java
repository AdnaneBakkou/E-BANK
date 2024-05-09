package ma.bakkou.projerebank.repositories;

import ma.bakkou.projerebank.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer,Long> {
}
