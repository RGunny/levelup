package me.rgunny.levelup.account.facade;

import me.rgunny.levelup.account.infrastructure.redis.RedisLockRepository;
import me.rgunny.levelup.account.service.AccountService;
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
        System.out.println("LettuceLockAccountFacade.withdraw");
        while (!redisLockRepository.lock(key)) {
            System.out.println("lock in and sleep");
            Thread.sleep(100);
        }
        System.out.println("key = " + key + "  quantity" + quantity);
        System.out.println();
        try {
            accountService.withdraw(key, quantity);
        } finally {
            redisLockRepository.unlock(key);
        }
    }
}
