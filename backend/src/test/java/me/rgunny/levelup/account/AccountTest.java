package me.rgunny.levelup.account;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class AccountTest {

    @DisplayName("Account는 AccountCreate 객체로 생성 할 수 있다.")
    @Test
    void createAccountByAccountCreate(){
        // given
        AccountCreate accountCreate = AccountCreate.builder()
                .id(1L)
                .name("나라사랑계좌")
                .password("1q2w3e4r!@")
                .build();

        // when
        Account account = Account.from(accountCreate);

        // then
        assertThat(account.getId()).isEqualTo(1L);
        assertThat(account.getName()).isEqualTo("나라사랑계좌");
        assertThat(account.getPassword()).isEqualTo("1q2w3e4r!@");
    }

}