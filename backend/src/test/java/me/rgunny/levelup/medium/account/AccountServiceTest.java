package me.rgunny.levelup.medium.account;

import me.rgunny.levelup.account.domain.Account;
import me.rgunny.levelup.account.infrastructure.AccountEntity;
import me.rgunny.levelup.account.infrastructure.AccountJpaRepository;
import me.rgunny.levelup.account.service.AccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.concurrent.*;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
//@TestPropertySource("classpath:test-application.yml")
public class AccountServiceTest {

    @Autowired
    private AccountServiceImpl accountService;

    @Autowired
    private AccountJpaRepository accountJpaRepository;


    @BeforeEach
    void init() {
        AccountEntity account = AccountEntity.builder()
                .id(1L)
                .number("0-1234-5678-9")
                .password("1q2w3e4r!@")
                .name("나라사랑계좌")
                .balance(0L)
                .build();
        AccountEntity account2 = AccountEntity.builder()
                .id(2L)
                .number("2-1234-5678-9")
                .password("1q2w3e4r!@")
                .name("나라사랑계좌2")
                .balance(10000L)
                .build();
        AccountEntity account3 = AccountEntity.builder()
                .id(3L)
                .number("3-1234-5678-9")
                .password("1q2w3e4r!@")
                .name("나라사랑계좌3")
                .balance(50000L)
                .build();

        accountJpaRepository.save(account);
        accountJpaRepository.save(account2);
        accountJpaRepository.save(account3);
    }

    @DisplayName("amount를 계좌에 입금하면 기존잔고에 amount만큼 잔고가 증가한다.")
    @Test
    void depositSuccessTest(){
        // given
        long amount = 10000L;

        // when
        Account account = accountService.deposit(1L, amount);

        // then
        assertThat(account.getBalance()).isEqualTo(10000L);
    }

    @DisplayName("amount를 계좌에서 출금하면 기존잔고에서 amount만큼 잔고가 차감된다.")
    @Test
    void withdrawSuccessTest(){
        // given
        long amount = 10000L;

        // when
        Account account = accountService.withdraw(2L, amount);

        // then
        assertThat(account.getBalance()).isEqualTo(0L);
    }

    @DisplayName("동시에 100번 출금할 경우 synchronized를 사용한 동시성 제어 성공 테스트")
    @Test
    void withdrawUsingSynchronizedConcurrentTest() throws InterruptedException {
        // given
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);

        // when
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    accountService.withdrawUsingSynchronized(2L, 100L);
                } finally {
                    countDownLatch.countDown();
                }
            });
        }

        countDownLatch.await();
        Account account = accountService.getById(2L);

        // then
        assertThat(account.getBalance()).isEqualTo(0L);
    }

    @DisplayName("동시에 100번 출금할 경우 pessimistic lock을 사용한 동시성 제어를 위한 성공 테스트")
    @Test
    void withdrawUsingPessimisticLockConcurrentTest() throws InterruptedException {
        // given
        int threadCount = 100, successCount = 0;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);

        // when
        for (int i = 0; i < threadCount; i++) {
            CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
                accountService.withdrawUsingPessimisticLock(2L, 100L);
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
        Account account = accountService.getById(2L);

        // then
        assertThat(successCount).isEqualTo(100);
        assertThat(account.getBalance()).isEqualTo(0L);
    }

}
