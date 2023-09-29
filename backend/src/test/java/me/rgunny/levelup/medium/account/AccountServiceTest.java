package me.rgunny.levelup.medium.account;

import me.rgunny.levelup.account.domain.Account;
import me.rgunny.levelup.account.domain.AccountCreate;
import me.rgunny.levelup.account.service.AccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
//@TestPropertySource("classpath:test-application.yml")
public class AccountServiceTest {

    @Autowired
    private AccountServiceImpl accountService;

    @BeforeEach
    void init() {
        AccountCreate account = AccountCreate.builder()
                .id(1L)
                .number("0-1234-5678-9")
                .password("1q2w3e4r!@")
                .name("나라사랑계좌")
                .build();
        AccountCreate account2 = AccountCreate.builder()
                .id(2L)
                .number("2-1234-5678-9")
                .password("1q2w3e4r!@")
                .name("나라사랑계좌2")
                .build();

        accountService.create(account);
        accountService.create(account2);

        accountService.deposit(2L, 10000L);
    }

    @DisplayName("동시에 100번 출금할 경우 pessimistic lock을 사용한 동시성 제어를 위한 테스트")
    @Test
    void withdrawUsingPessimisticLockConcurrentTest() throws InterruptedException {
        Account account1 = accountService.getById(2L);

        // given
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);

        // when
        for (int i = 0; i < threadCount; i++) {
            int finalI = i;
            executorService.submit(() -> {
                try {
                    Account account = accountService.withdrawUsingPessimisticLock(2L, 100L);
                    System.out.println(finalI + "i : " + account.getBalance());
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

}
