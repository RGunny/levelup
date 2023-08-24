package me.rgunny.levelup.account;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Account {

    private final Long id;
    private final String number; // bank account number
    private final String password;
    private final String name;
    private final long balance;

    @Builder
    public Account(Long id, String number, String password, String name, long balance) {
        this.id = id;
        this.number = number;
        this.password = password;
        this.name = name;
        this.balance = balance;
    }

    public static Account from(AccountCreate accountCreate) {
        return Account.builder()
                .id(accountCreate.getId())
                .number(accountCreate.getNumber())
                .password(accountCreate.getPassword())
                .name(accountCreate.getName())
                .balance(0L)
                .build();
    }

    public Account update(AccountUpdate accountUpdate) {
        return Account.builder()
                .id(id)
                .number(accountUpdate.getNumber())
                .name(accountUpdate.getName())
                .password(accountUpdate.getPassword())
                .balance(balance)
                .build();
    }
}
