package me.rgunny.levelup.account.infrastructure;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.rgunny.levelup.account.domain.Account;

@Entity
@Table(name = "accounts_version")
@Getter
@NoArgsConstructor
public class AccountVersionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String number; // bank account number

    private String password;

    private String name;

    private long balance;

    @Version
    private Long version;

    @Builder
    public AccountVersionEntity(Long id, String number, String password, String name, long balance, Long version) {
        this.id = id;
        this.number = number;
        this.password = password;
        this.name = name;
        this.balance = balance;
        this.version = version;
    }

    public void withdraw(long amount) {
        if (balance - amount < 0) {
            throw new IllegalArgumentException("출금 금액이 잔고를 초과할 수 없습니다.");
        }

        this.balance = balance - amount;
    }

    public static AccountVersionEntity from(Account account) {
        return AccountVersionEntity.builder()
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