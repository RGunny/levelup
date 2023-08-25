package me.rgunny.levelup.account.infrastructure;

import me.rgunny.levelup.account.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountJpaRepository extends JpaRepository<Account, Long> {
}
