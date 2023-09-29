package me.rgunny.levelup.account.service.port;

import me.rgunny.levelup.account.domain.Account;

import java.util.Optional;

public interface AccountRepository {

    Optional<Account> findById(Long id);

    Optional<Account> findByIdWithPessimisticLock(Long id);

    Account save(Account account);
}
