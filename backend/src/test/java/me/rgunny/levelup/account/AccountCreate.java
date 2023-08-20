package me.rgunny.levelup.account;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AccountCreate {

    private String password;
    private String name;

    public static AccountCreate from(Account account) {
        return AccountCreate.builder()
                .password(account.getPassword())
                .name(account.getName())
                .build();
    }
}
