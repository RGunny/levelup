package me.rgunny.levelup.account.infrastructure;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.rgunny.levelup.account.domain.Account;

@Entity
@Table(name = "accounts")
@Getter
@NoArgsConstructor
public class AccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String number; // bank account number

    private String password;

    private String name;

    private long balance;

    @Builder
    public AccountEntity(Long id, String number, String password, String name, long balance) {
        this.id = id;
        this.number = number;
        this.password = password;
        this.name = name;
        this.balance = balance;
    }

    public static AccountEntity from(Account account) {
        return AccountEntity.builder()
                .id(account.getId())
                .number(account.getNumber())
                .password(account.getPassword())
                .name(account.getName())
                .balance(account.getBalance())
                .build();
    }

    public Account toModel() {
        return Account.builder()
                .id(id)
                .number(number)
                .password(password)
                .name(name)
                .balance(balance)
                .build();
    }

}
