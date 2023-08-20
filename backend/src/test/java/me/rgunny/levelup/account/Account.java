package me.rgunny.levelup.account;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Account {

    private final Long id;
    private final String password;
    private final String name;
    private final long balance;

    @Builder
    public Account(Long id, String password, String name, long balance) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.balance = balance;
    }

    public static Account from(AccountCreate accountCreate) {
        return Account.builder()
                .id(accountCreate.getId())
                .password(accountCreate.getPassword())
                .name(accountCreate.getName())
                .balance(0L)
                .build();
    }
}
