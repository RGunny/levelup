package me.rgunny.levelup.account.facade;

import lombok.RequiredArgsConstructor;
import me.rgunny.levelup.account.service.AccountServiceImpl;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OptimisticLockAccountFacade {

    private final AccountServiceImpl accountService;

    public void withdraw(Long id, Long amount) throws InterruptedException {
        while (true) {
            try {
                accountService.withdrawUsingOptimisticLock(id, amount);
                break;
            } catch (Exception e) {
                Thread.sleep(50);
            }
        }
    }
}
