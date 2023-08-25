package me.rgunny.levelup.account.infrastructure;

import me.rgunny.levelup.account.domain.Account;
import me.rgunny.levelup.account.service.port.AccountRepository;

import java.util.Optional;

public class AccountRepositoryImpl implements AccountRepository {

    private AccountJpaRepository accountJpaRepository;

    @Override
    public Optional<Account> findById(Long id) {
        return accountJpaRepository.findById(id).map(AccountEntity::toModel);
    }

    @Override
    public Account save(Account account) {
        return accountJpaRepository.save(AccountEntity.from(account)).toModel();
    }
}
