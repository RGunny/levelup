package me.rgunny.levelup.account.facade;

import me.rgunny.levelup.account.infrastructure.AccountJpaRepository;
import me.rgunny.levelup.account.service.AccountServiceImpl;
import org.springframework.stereotype.Component;

@Component
public class NamedLockAccountFacade {

    private AccountServiceImpl accountService;
    private AccountJpaRepository accountJpaRepository;

    public NamedLockAccountFacade(AccountServiceImpl accountService, AccountJpaRepository accountJpaRepository) {
        this.accountService = accountService;
        this.accountJpaRepository = accountJpaRepository;
    }

    public void withdraw(Long id, Long quantity) {
        try {
            accountJpaRepository.getLock(id.toString());
            accountService.withdraw(id, quantity);
        } finally {
            accountJpaRepository.releaseLock(id.toString());
        }
    }
}
