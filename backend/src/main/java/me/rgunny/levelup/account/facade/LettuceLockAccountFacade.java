package me.rgunny.levelup.account.facade;

import me.rgunny.levelup.account.infrastructure.redis.RedisLockRepository;
import me.rgunny.levelup.account.controller.port.AccountService;
import org.springframework.stereotype.Component;

@Component
public class LettuceLockAccountFacade {

    private AccountService accountService;
    private RedisLockRepository redisLockRepository;

    public LettuceLockAccountFacade(RedisLockRepository redisLockRepository, AccountService accountService) {
        this.redisLockRepository = redisLockRepository;
        this.accountService = accountService;
    }

    public void withdraw(Long key, Long quantity) throws InterruptedException {
        while (!redisLockRepository.lock(key)) {
            Thread.sleep(50);
        }

        try {
            accountService.withdraw(key, quantity);
        } finally {
            redisLockRepository.unlock(key);
        }
    }
}
