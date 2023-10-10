package me.rgunny.levelup.account.facade;

import me.rgunny.levelup.account.infrastructure.AccountLockRepository;
import me.rgunny.levelup.account.service.AccountService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class NamedLockAccountFacade {

    private AccountLockRepository accountLockRepository;

    private AccountService accountService;

    public NamedLockAccountFacade(AccountLockRepository accountLockRepository, AccountService stockService) {
        this.accountLockRepository = accountLockRepository;
        this.accountService = stockService;
    }

    @Transactional
    public void withdraw(Long id, Long quantity) {
        try {
            accountLockRepository.getLock(id.toString());
            accountService.withdraw(id, quantity);
        } finally {
            accountLockRepository.releaseLock(id.toString());
        }
    }
}
