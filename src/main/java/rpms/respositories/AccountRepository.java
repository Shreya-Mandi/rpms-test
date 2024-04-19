package rpms.respositories;

import org.springframework.data.jpa.repository.JpaRepository;
import rpms.models.Account;
import rpms.models.enums.AccountType;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, String> {
    List<Account> findAllByIsApprovedIsFalseAndTypeIs(AccountType accountType);

    List<Account> findAllByIsApprovedIsTrueAndTypeIs(AccountType accountType);
}
