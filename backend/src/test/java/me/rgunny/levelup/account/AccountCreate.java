package me.rgunny.levelup.account;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AccountCreate {

    private Long id;
    private String password;
    private String name;

    public static AccountCreate from(Account account) {
        return AccountCreate.builder()
                .id(account.getId())
                .password(account.getPassword())
                .name(account.getName())
                .build();
    }

}
