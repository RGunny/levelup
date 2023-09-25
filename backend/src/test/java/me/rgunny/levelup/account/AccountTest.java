package me.rgunny.levelup.account;

import me.rgunny.levelup.account.domain.Account;
import me.rgunny.levelup.account.domain.AccountCreate;
import me.rgunny.levelup.account.domain.AccountUpdate;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AccountTest {

    @DisplayName("AccountCreate 로 Account 객체를 생성 할 수 있다.")
    @Test
    void createAccountByAccountCreate(){
        // given
        AccountCreate accountCreate = getAccountCreate();

        // when
        Account account = Account.from(accountCreate);

        // then
        assertThat(account.getId()).isEqualTo(1L);
        assertThat(account.getNumber()).isEqualTo("0-1234-5678-9");
        assertThat(account.getName()).isEqualTo("나라사랑계좌");
        assertThat(account.getPassword()).isEqualTo("1q2w3e4r!@");
    }

    @DisplayName("AccountUpdate 로 Account 객체를 수정 할 수 있다.")
    @Test
    void updateAccountByAccountUpdate(){
        // given
        Account account = Account.from(getAccountCreate());
        AccountUpdate accountUpdate = AccountUpdate.builder()
                .number("0-1234-5678-9")
                .name("나라안사랑계좌")
                .password("qweqwe12#")
                .build();
        // when
        Account updatedAccount = account.update(accountUpdate);

        // then
        assertThat(updatedAccount.getNumber()).isEqualTo("0-1234-5678-9");
        assertThat(updatedAccount.getName()).isEqualTo("나라안사랑계좌");
        assertThat(updatedAccount.getPassword()).isEqualTo("qweqwe12#");
    }

    @DisplayName("계좌에 입금 시 금액만큼 잔고가 증가한다.")
    @Test
    void depositTest(){
        // given
        Account account = Account.from(getAccountCreate());
        long amount = 10000L;

        // when
        Account depositedAccount = account.deposit(amount);

        // then
        assertThat(account.getBalance() + amount).isEqualTo(depositedAccount.getBalance());
    }

    @DisplayName("계좌의 잔고 금액을 초과하지 않을 경우 출금할 수 있다.")
    @Test
    void withdrawWhenNotExceedBalance(){
        // given
        Account account = Account.from(getAccountCreate());
        long amount = 10000L;
        account = account.deposit(amount);

        // when
        Account withdrawedAccount = account.withdraw(10000L);

        // then
        assertThat(withdrawedAccount.getBalance()).isEqualTo(0L);
    }
    @DisplayName("계좌의 잔고 금액을 초과할 경우 IllegalArgumentException 이 발생한다.")
    @Test
    void withdrawOccurExceptionWhenExceedBalance(){
        // given
        Account account = Account.from(getAccountCreate());
        long amount = 10000;
        Account depositedAccount = account.deposit(amount);

        // when
        // then
        Assertions.assertThatThrownBy(() -> depositedAccount.withdraw(20000))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("출금 금액이 잔고를 초과할 수 없습니다.");
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