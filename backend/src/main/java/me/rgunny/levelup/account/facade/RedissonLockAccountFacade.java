package me.rgunny.levelup.account.facade;

import me.rgunny.levelup.account.service.AccountService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedissonLockAccountFacade {

    private RedissonClient redissonClient;

    private AccountService accountService;

    public RedissonLockAccountFacade(RedissonClient redissonClient, AccountService stockService) {
        this.redissonClient = redissonClient;
        this.accountService = stockService;
    }

    public void withdraw(Long key, Long quantity) {
        RLock lock = redissonClient.getLock(key.toString());

        try {
            boolean available = lock.tryLock(10, 1, TimeUnit.SECONDS);

            if (!available) {
                System.out.println("lock 획득 실패");
                return;
            }

            accountService.withdraw(key, quantity);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }
}