package gr.rongasa.agregator.repository;

import gr.rongasa.agregator.domain.jpa.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, String> {
}
