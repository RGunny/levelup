package me.rgunny.levelup.account;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AccountTest {

    private AccountService accountService;


    @DisplayName("AccountCreate 로 계좌를 생성 할 수 있다.")
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

    @DisplayName("AccountUpdate 로 계좌를 수정 할 수 있다.")
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

    private AccountCreate getAccountCreate() {
        return AccountCreate.builder()
                .id(1L)
                .number("0-1234-5678-9")
                .name("나라사랑계좌")
                .password("1q2w3e4r!@")
                .build();
    }

}