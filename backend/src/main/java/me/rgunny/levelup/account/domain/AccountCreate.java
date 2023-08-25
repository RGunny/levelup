package me.rgunny.levelup.account.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AccountCreate {

    private final Long id;
    private final String number;
    private final String password;
    private final String name;

    public static AccountCreate from(Account account) {
        return AccountCreate.builder()
                .id(account.getId())
                .number(account.getNumber())
                .password(account.getPassword())
                .name(account.getName())
                .build();
    }

}
