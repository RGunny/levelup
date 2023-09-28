package me.rgunny.levelup.account.service;

import me.rgunny.levelup.account.domain.Account;
import me.rgunny.levelup.account.domain.AccountCreate;
import me.rgunny.levelup.mock.FakeAccountRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

class AccountServiceImplTest {

    private AccountServiceImpl accountServiceImpl;

    @BeforeEach
    void init() {
        FakeAccountRepository fakeAccountRepository = new FakeAccountRepository();
        accountServiceImpl = AccountServiceImpl.builder()
                .accountRepository(fakeAccountRepository)
                .build();

        Account account = Account.builder()
                .id(1L)
                .number("0-1234-5678-9")
                .password("1q2w3e4r!@")
                .name("나라사랑계좌")
                .balance(0L)
                .build();
        Account account2 = Account.builder()
                .id(2L)
                .number("2-1234-5678-9")
                .password("1q2w3e4r!@")
                .name("나라사랑계좌2")
                .balance(10000L)
                .build();

        fakeAccountRepository.save(account);
        fakeAccountRepository.save(account2);
    }

    @DisplayName("AccountCreate 로 계좌를 생성할 수 있다.")
    @Test
    void createAccountByAccountCreate(){
        // given
        AccountCreate accountCreate = getAccountCreate();

        // when
        Account account = accountServiceImpl.create(accountCreate);

        // then
        assertThat(account.getId()).isEqualTo(1L);
        assertThat(account.getNumber()).isEqualTo("0-1234-5678-9");
        assertThat(account.getName()).isEqualTo("나라사랑계좌");
        assertThat(account.getPassword()).isEqualTo("1q2w3e4r!@");
        assertThat(account.getBalance()).isEqualTo(0L);
    }

    @DisplayName("id 에 해당하는 계좌를 찾아준다.")
    @Test
    void getByIdSuccessTest(){
        // given -> init

        // when
        Account account = accountServiceImpl.getById(1L);

        // then
        assertThat(account.getId()).isEqualTo(1L);
        assertThat(account.getNumber()).isEqualTo("0-1234-5678-9");
        assertThat(account.getName()).isEqualTo("나라사랑계좌");
        assertThat(account.getPassword()).isEqualTo("1q2w3e4r!@");
        assertThat(account.getBalance()).isEqualTo(0L);
    }

    @DisplayName("amount를 계좌에 입금하면 기존잔고에 amount만큼 잔고가 증가한다.")
    @Test
    void depositSuccessTest(){
        // given
        long amount = 10000L;

        // when
        Account account = accountServiceImpl.deposit(1L, amount);

        // then
        assertThat(account.getBalance()).isEqualTo(10000L);
    }

    @DisplayName("amount를 계좌에서 출금하면 기존잔고에서 amount만큼 잔고가 차감된다.")
    @Test
    void withdrawSuccessTest(){
        // given
        long amount = 10000L;

        // when
        Account account = accountServiceImpl.withdraw(2L, amount);

        // then
        assertThat(account.getBalance()).isEqualTo(0L);
    }

    @DisplayName("amount를 계좌에서 출금할 때 기존잔고보다 amount 가 초과하면 예외가 발생한다.")
    @Test
    void withdrawOccurExceptionWhenExceedBalance(){
        // given
        long amount = 20000L;

        // when
        // then
        Assertions.assertThatThrownBy(() -> accountServiceImpl.withdraw(2L, 20000L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("출금 금액이 잔고를 초과할 수 없습니다.");
    }

    @DisplayName("동시에 100번 출금할 경우 동시성 제어를 위한 테스트")
    @Test
    void withdrawConcurrentTest() throws InterruptedException {
        // given
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);

        // when
        for (int i = 0; i < threadCount; i++) {
            int finalI = i;
            executorService.submit(() -> {
                try {
                    Account a = accountServiceImpl.withdraw(2L, 100L);
                } finally {
                    countDownLatch.countDown();
                }
            });
        }

        countDownLatch.await();
        Account account = accountServiceImpl.getById(2L);

        // then
        assertThat(account.getBalance()).isEqualTo(0L);
    }

    private AccountCreate getAccountCreate() {
        return AccountCreate.builder()
                .id(1L)
                .number("0-1234-5678-9")
                .name("나라사랑계좌")
                .password("1q2w3e4r!@")
                .build();
    }

}