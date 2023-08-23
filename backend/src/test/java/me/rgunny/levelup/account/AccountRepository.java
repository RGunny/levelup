package me.rgunny.levelup.account;

import java.util.Optional;

public interface AccountRepository {

    Optional<Account> findById(Long id);

    Account save(Account account);
}
