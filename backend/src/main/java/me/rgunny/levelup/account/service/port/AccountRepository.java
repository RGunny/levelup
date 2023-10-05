package me.rgunny.levelup.account.service.port;

import me.rgunny.levelup.account.domain.Account;

import java.util.Optional;

public interface AccountRepository {

    Account save(Account account);

    Account saveAndFlush(Account account);


    Optional<Account> findById(Long id);

    Optional<Account> findByIdUsingPessimisticLock(Long id);

}
