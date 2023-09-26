package me.rgunny.levelup.account.service;

import me.rgunny.levelup.account.domain.Account;
import me.rgunny.levelup.account.domain.AccountCreate;
import me.rgunny.levelup.mock.FakeAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AccountServiceTest {

    private AccountService accountService;

    @BeforeEach
    void init() {
        FakeAccountRepository fakeAccountRepository = new FakeAccountRepository();
        accountService = AccountService.builder()
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
        Account account = accountService.create(accountCreate);

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
        Account account = accountService.getById(1L);

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

    private AccountCreate getAccountCreate() {
        return AccountCreate.builder()
                .id(1L)
                .number("0-1234-5678-9")
                .name("나라사랑계좌")
                .password("1q2w3e4r!@")
                .build();
    }

}