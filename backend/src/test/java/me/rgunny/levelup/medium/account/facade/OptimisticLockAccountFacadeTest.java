package me.rgunny.levelup.medium.account.facade;

import me.rgunny.levelup.account.facade.OptimisticLockAccountFacade;
import me.rgunny.levelup.account.infrastructure.version.AccountVersionEntity;
import me.rgunny.levelup.account.infrastructure.version.AccountVersionJpaRepository;
import me.rgunny.levelup.medium.DatabaseCleanup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.concurrent.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class OptimisticLockAccountFacadeTest {

    @Autowired
    private DatabaseCleanup databaseCleanup;

    @Autowired
    private OptimisticLockAccountFacade optimisticLockAccountFacade;

    @Autowired
    private AccountVersionJpaRepository accountVersionJpaRepository;

    @BeforeEach
    void init() {
        databaseCleanup.afterPropertiesSet();
        databaseCleanup.execute();

        AccountVersionEntity account = AccountVersionEntity.builder()
                .id(1L)
                .number("0-1234-5678-9")
                .password("1q2w3e4r!@")
                .name("나라사랑계좌")
                .balance(0L)
                .build();
        AccountVersionEntity account2 = AccountVersionEntity.builder()
                .id(2L)
                .number("2-1234-5678-9")
                .password("1q2w3e4r!@")
                .name("나라사랑계좌2")
                .balance(10000L)
                .build();
        AccountVersionEntity account3 = AccountVersionEntity.builder()
                .id(3L)
                .number("3-1234-5678-9")
                .password("1q2w3e4r!@")
                .name("나라사랑계좌3")
                .balance(50000L)
                .build();

        accountVersionJpaRepository.save(account);
        accountVersionJpaRepository.save(account2);
        accountVersionJpaRepository.save(account3);
    }

    @DisplayName("동시에 100번 출금할 경우 optimistic lock을 사용한 동시성 제어를 위한 성공 테스트")
    @Test
    void withdrawUsingOptimisticLockConcurrentTest() throws InterruptedException {
        // given
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);

        // when
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    optimisticLockAccountFacade.withdraw(2L, 100L);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    countDownLatch.countDown();
                }
            });
        }

        countDownLatch.await();
        AccountVersionEntity account = accountVersionJpaRepository.findById(2L).orElseThrow();

        // then
        assertThat(account.getBalance()).isEqualTo(0L);
    }

    @DisplayName("동시에 100번 출금할 경우 optimistic lock을 사용한 동시성 제어를 위한 CompletableFuture 사용한 성공 테스트 ")
    @Test
    void withdrawUsingOptimisticLockConcurrentTest2() throws InterruptedException {
        // given
        int threadCount = 100, successCount = 0;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);

        // when
        for (int i = 0; i < threadCount; i++) {
            CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
                try {
                    optimisticLockAccountFacade.withdraw(2L, 100L);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                return "Thread: " + Thread.currentThread().getName();
            }, executorService).exceptionally(e -> {
                System.out.println("e.getMessage() = " + e.getMessage());
                throw new RuntimeException("RuntimeException!");
            });

            try {
                completableFuture.join();
                successCount++;
            } catch (CompletionException e) {
                throw e;
            } finally {
                countDownLatch.countDown();
            }
        }

        countDownLatch.await();
        AccountVersionEntity account = accountVersionJpaRepository.findById(2L).get();

        // then
        assertThat(successCount).isEqualTo(100);
        assertThat(account.getBalance()).isEqualTo(0L);
    }

}
