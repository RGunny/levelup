package me.rgunny.levelup.account.infrastructure;

import lombok.RequiredArgsConstructor;
import me.rgunny.levelup.account.domain.Account;
import me.rgunny.levelup.account.service.port.AccountRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AccountRepositoryImpl implements AccountRepository {

    private final AccountJpaRepository accountJpaRepository;

    @Override
    public Optional<Account> findById(Long id) {
        return accountJpaRepository.findById(id).map(AccountEntity::toModel);
    }

    @Override
    public Account save(Account account) {
        return accountJpaRepository.save(AccountEntity.from(account)).toModel();
    }
}
