package me.rgunny.levelup.account.domain;

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

    public Account deposit(long amount) {
        return Account.builder()
                .id(id)
                .number(number)
                .password(password)
                .name(name)
                .balance(balance + amount)
                .build();
    }

    // TODO: Transfer로 Account에 출금&입금 이벤트를 발생시킨다. 하지만 Account 는 출금&입금 각각의 도메인서비스만 제공하는 것이고 Transfer와는 상관없는 분리된 영역이다.
    public Account withdraw(long amount) {
        if (balance - amount < 0) {
            throw new IllegalArgumentException("출금 금액이 잔고를 초과할 수 없습니다.");
        }

        return Account.builder()
                .id(id)
                .number(number)
                .password(password)
                .name(name)
                .balance(balance - amount)
                .build();
    }

}
