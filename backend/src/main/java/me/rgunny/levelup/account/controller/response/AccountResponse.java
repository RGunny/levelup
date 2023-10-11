package me.rgunny.levelup.account.controller.response;

import lombok.Builder;
import lombok.Getter;
import me.rgunny.levelup.account.domain.Account;

@Getter
@Builder
public class AccountResponse {

    private Long id;
    private String number; // bank account number
    private String password;
    private String name;
    private long balance;

    public static AccountResponse from(Account account) {
        return AccountResponse.builder()
                .id(account.getId())
                .number(account.getNumber())
                .password(account.getPassword())
                .name(account.getName())
                .build();
    }

}
